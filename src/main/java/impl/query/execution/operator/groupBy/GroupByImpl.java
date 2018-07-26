package impl.query.execution.operator.groupBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import dataset.ColumnDescriptor;
import dataset.IDataSet;
import dataset.ILayoutManager;
import datatype.DataType;
import model.FieldDescriptor;
import query.builder.predicate.AggregateFunction;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.AggregationDescriptor;
import query.builder.statement.CFNode;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;
import utils.RecordAggregator;
import utils.RecordEvaluator;

public class GroupByImpl  extends GroupByFunction{
	
	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {

		Stream<Object[]> recordStream = inputDataSet.getRecordStream();
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		List<AggregationDescriptor> aggregations = Lists.newArrayList(args.getAggregations());
		List<AggregateFilterStatement> aggregateFilters = args.getAggregateFIlters();
		
		int[] fieldSequenceIndexes = 
				getGroupingSequenceIndexes(inputDataSet,groupingSequence);
		
		List<Object[]> resultRecords;
		if(aggregations.size() == 0) {
			resultRecords =
					groupRecords(recordStream, fieldSequenceIndexes);
		}else {
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector = 
					getDownStreamCollector(aggregations,inputDataSet);
			resultRecords =
					aggregateRecords(recordStream,fieldSequenceIndexes,downStreamCollector);
		}
		
		if(aggregateFilters.size() != 0) {
			Map<String,Integer> aggrNameIndexMapping = mapAggregatesToIndexes(aggregations,groupingSequence.size());
			 resultRecords = 
					filterRecords(aggrNameIndexMapping,aggregateFilters,resultRecords);
		}
		
		List<ColumnDescriptor> columnSequence = getResultColumnSequence(groupingSequence,aggregations);
		IDataSet result = layoutManager.buildDataSet(
				resultRecords.size(),
				columnSequence,
				resultRecords.iterator()
		);
		
		return result;
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

	
	private Collector<Object[], RecordAggregator, Object[]> getDownStreamCollector(List<AggregationDescriptor> aggregations, IDataSet inputDataSet) {
		Supplier<RecordAggregator> supplier = getSupplier(aggregations,inputDataSet);
		BiConsumer<RecordAggregator, Object[]> accumulator = getAccumulator();
		BinaryOperator<RecordAggregator> combiner = getCombiner();
		Function<RecordAggregator,Object[]> finalizer = getFinalizer(aggregations);
		
		return Collector.of(supplier,accumulator,combiner ,finalizer);
	}

	
	private Supplier<RecordAggregator> getSupplier(List<AggregationDescriptor> aggregations, IDataSet inputDataSet) {
		return new Supplier<RecordAggregator>() {
			@Override
			public RecordAggregator get() {
				return new RecordAggregator(aggregations, inputDataSet);
			}
		};
	}	
	
	private BiConsumer<RecordAggregator, Object[]> getAccumulator() {
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
	
	private Function<RecordAggregator, Object[]> getFinalizer (List<AggregationDescriptor> aggregations) {
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
	
	
	
	private List<Object[]> aggregateRecords(
			Stream<Object[]> recordStream, 
			int[] fieldSequenceIndexes,
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector
	){
		Map<List<Object>, Object[]> resultMap =
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
			for(Map.Entry<List<Object>,Object[]> entry : resultMap.entrySet()) {
				List<Object> key = entry.getKey();
				Object[] value = entry.getValue();
				Object[] currentRecord = new Object[key.size() + value.length];
				for(int i=0; i<key.size(); i++) {
					currentRecord[i] = key.get(i);
				}
				for(int i=key.size(); i < (key.size() + value.length); i++) {
					currentRecord[i] = value[ i-key.size()];
				}
				
				if((Double)currentRecord[key.size()] > 22) {
					result.add(currentRecord);
				}
			}
			
			return result;
	}


	
	private Map<String, Integer> mapAggregatesToIndexes(List<AggregationDescriptor> aggregations, int aggrSequenceSize) {
		Map<String,Integer> mapping = new HashMap<>();
		int index = 0;
		for(AggregationDescriptor aggregation : aggregations) {
			mapping.put(aggregation.getKey(), index+aggrSequenceSize);
			index ++;
		}
		return mapping;
	}
	

	private List<Object[]> filterRecords(
			Map<String,Integer> aggrNameIndexMapping,
			List<AggregateFilterStatement> aggregateFilters, 
			List<Object[]> groupedRecords
	){
		Set<CFNode> statements = Sets.newHashSet(aggregateFilters);
		RecordEvaluator evaluator = new RecordEvaluator(aggrNameIndexMapping, statements);
		
		List<Object[]> filteredRecords = groupedRecords
		.stream()
		.filter(
			(Object[] record) -> evaluator.evaluate(record)
		)
		.collect(Collectors.toList());
		
		return filteredRecords;
	}
	
	
	private List<ColumnDescriptor> getResultColumnSequence(
			List<FieldDescriptor> groupingSequence,
			List<AggregationDescriptor> aggregations
	){
		List<ColumnDescriptor> sequence = new ArrayList<>();
		for (FieldDescriptor field : groupingSequence) {
			ColumnDescriptor currentColumn = new ColumnDescriptor(
				field.getTable().getName(), 
				field.getName(), 
				field.getType()
				);
			sequence.add(currentColumn);
		}
		for (AggregationDescriptor aggregateField : aggregations) {
			ColumnDescriptor currentColumn = new ColumnDescriptor(
					aggregateField.getField().getTable().getName(), 
					aggregateField.getField().getName(), 
					DataType.DOUBLE
					);
				sequence.add(currentColumn);
		}
		
		return sequence;
	}

}