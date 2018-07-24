package impl.query.execution.operator.groupBy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import dataset.IDataSet;
import dataset.ILayoutManager;
import model.FieldDescriptor;
import query.builder.statement.AggregationDescriptor;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;
import utils.RecordAggregator;

public class GroupByImpl  extends GroupByFunction{

	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {
		
		Stream<Object[]> recordStream = inputDataSet.getRecordStream();
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		Set<AggregationDescriptor> aggregations = args.getAggregations();
		
		int[] fieldSequenceIndexes = 
				getGroupingSequenceIndexes(inputDataSet,groupingSequence);
		
		if(aggregations.size() == 0) {
			List<Object[]> groupedRecords =
					groupRecords(recordStream, fieldSequenceIndexes);
		}else {
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector = 
					getDownStreamCollector(aggregations,inputDataSet);
			List<Object[]> aggregateRecords =
					aggregateRecords(recordStream,fieldSequenceIndexes,downStreamCollector);
		}
		
		return null;
	}


	private Collector<Object[], RecordAggregator, Object[]> getDownStreamCollector(Set<AggregationDescriptor> aggregations, IDataSet inputDataSet) {
		Supplier<RecordAggregator> initializer = getInitializer(aggregations,inputDataSet);
		BiConsumer<RecordAggregator, Object[]> aggregator = getAggregator();
		BinaryOperator<RecordAggregator> combiner = getCombiner();
		Function<RecordAggregator,Object[]> finalizer = getFinalizer();
		
		return Collector.of(initializer,aggregator,combiner ,finalizer);
	}

	private int[] getGroupingSequenceIndexes(IDataSet inputDataSet, List<FieldDescriptor> groupingSequence) {
		int[] fieldSequenceIndexes = new int[groupingSequence.size()];
		int index = 0;
		for( FieldDescriptor field : groupingSequence) {
			fieldSequenceIndexes[index] = inputDataSet.getColumnIndex(field);
			index ++;
		}
		return fieldSequenceIndexes;
	}

	private Supplier<RecordAggregator> getInitializer(Set<AggregationDescriptor> aggregations, IDataSet inputDataSet) {
		return new Supplier<RecordAggregator>() {
			@Override
			public RecordAggregator get() {
				return new RecordAggregator(aggregations, inputDataSet);
			}
		};
	}	
	
	private BiConsumer<RecordAggregator, Object[]> getAggregator() {
		return new BiConsumer<RecordAggregator, Object[]>() {
			@Override
			public void accept(RecordAggregator recordAggregator, Object[] record) {
				recordAggregator.aggregateRecord(record);
			}
		};
	}
	
	private BinaryOperator<RecordAggregator> getCombiner() {
		return new BinaryOperator<RecordAggregator>() {
			@Override
			public RecordAggregator apply(RecordAggregator aggregator, RecordAggregator other) {
				RecordAggregator combinedAggregator = aggregator.combine(other);
				return combinedAggregator;
			}
		};
	}
	
	private Function<RecordAggregator, Object[]> getFinalizer() {
		return new Function<RecordAggregator, Object[]>() {
			@Override
			public Object[] apply(RecordAggregator aggregator) {
				return aggregator.getAggregationResult();
			}
		};
	}
	
	
	
	private List<Object[]> groupRecords(Stream<Object[]> recordStream, int[] fieldSequenceIndexes) {
		
		Map<Object, List<Object[]>> resultMap =
		recordStream
		.collect(
			Collectors.groupingBy (
				record -> {
					List<Object> groupingKey = new ArrayList<>();
					for(int i=0; i<fieldSequenceIndexes.length; i++) {
						groupingKey.add(record[fieldSequenceIndexes[i]]);
					} 
					return groupingKey;
				},
				LinkedHashMap::new,
				Collectors.toList()
			)
		);
	
		List<Object[]> result = new ArrayList<>();
		for(List<Object[]> partialResult : resultMap.values()) {
			result.addAll(partialResult);
		}
		
		return result;
	}
	
	
	private List<Object[]> aggregateRecords(Stream<Object[]> recordStream, int[] fieldSequenceIndexes,
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector) {
		Map<Object, Object[]> resultMap =
			recordStream
			.collect(
				Collectors.groupingBy (
					record -> {
						List<Object> groupingKey = new ArrayList<>();
						for(int i=0; i<fieldSequenceIndexes.length; i++) {
							groupingKey.add(record[fieldSequenceIndexes[i]]);
						} 
						return groupingKey;
					},
					downStreamCollector
					)
			);
			
			List<Object[]> result = new ArrayList<>();
			for(Object[] partialResult : resultMap.values()) {
				result.add(partialResult);
			}
			
			return result;
	}

}
