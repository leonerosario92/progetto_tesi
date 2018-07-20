package impl.query.execution.operator.groupBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Lists;

import dataset.ColumnDescriptor;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import model.FieldDescriptor;
import query.builder.statement.AggregationDescriptor;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;

public class GroupByImpl  extends GroupByFunction{

	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {
		
		Stream<Object[]> recordStream = inputDataSet.getRecordStream();
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		Set<AggregationDescriptor> aggregations = args.getAggregations();
		
		int[] fieldSequenceIndexes = 
				getGroupingSequenceIndexes(inputDataSet,groupingSequence);
		
		Long start = System.nanoTime();
		Map<List<Object>, List<Object[]>> groupedRecords =
				groupRecords(recordStream, fieldSequenceIndexes);
		Long end = System.nanoTime();
		Float timeMs = new Float((end - start) / (1000 * 1000));
		
		
//		List<Object[]> aggregateRecords =
//			groupedRecords.entrySet().stream()
//			.map( 
//				entry -> {
//						Object[] aggregateRecord = new Object[3];
//						aggregateRecord[0] = entry.getKey().get(0);
//						aggregateRecord[1] = entry.getKey().get(1);
//						
//						RecordAggregator aggregator = new RecordAggregator(0);
//						entry.getValue().forEach(
//							record -> {
//								aggregator.aggregateRecord(record);
//							}
//						);
//						aggregateRecord[2] = aggregator.getSum();
//						return aggregateRecord;
//				}
//			)
//			.filter(record ->  {  return((long)record[2] > 22);  })
//			.collect(Collectors.toList());
//
//		
//		
//		List<ColumnDescriptor> columnSequence = new ArrayList<>();
//		//int newRecordCount = aggregateRecords.size();
//		columnSequence.add(inputDataSet.getColumn(groupingSequence.get(0)).getDescriptor());
//		columnSequence.add(inputDataSet.getColumn(groupingSequence.get(1)).getDescriptor());
//		columnSequence.add(inputDataSet.getColumnDescriptor(1));
//		
//		
//		
//		IDataSet result = layoutManager.buildDataSet(newRecordCount, columnSequence, aggregateRecords.iterator());
//		
//		System.out.println("Result : ");
//		aggregateRecords.stream()
//		.forEach( 
//				record ->{
//					StringBuilder sb = new StringBuilder();
//					for(int i=0; i<2; i++) {
//						sb.append("\t\t"+record[i]);
//					}
//					
//					System.out.println(sb.toString());
//				}
//		);
			
		return null;
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
	
	
	
	
	private Map<List<Object>, List<Object[]>> groupRecords(Stream<Object[]> recordStream, int[] groupingSequenceIndexes) {
		
		Supplier<LongSummaryStatistics> initializer = new Supplier<LongSummaryStatistics>() {
			@Override
			public LongSummaryStatistics get() {
				return new LongSummaryStatistics();
			}
		};
		
		
		BiConsumer<LongSummaryStatistics, Object[]> aggregator = new BiConsumer<LongSummaryStatistics, Object[]>() {
			@Override
			public void accept(LongSummaryStatistics aggregator, Object[] value) {
				aggregator.accept( ((BigDecimal)value[0]).longValue() );
			}
		};
		
		
		BinaryOperator<LongSummaryStatistics> combiner = new BinaryOperator<LongSummaryStatistics>() {
			@Override
			public LongSummaryStatistics apply(LongSummaryStatistics arg0, LongSummaryStatistics arg1) {
				 arg0.combine(arg1);
				 return arg0;
			}
		};
		
		
		Function<LongSummaryStatistics,Long> finalizer = new Function<LongSummaryStatistics, Long>() {
			@Override
			public Long apply(LongSummaryStatistics aggregator) {
				return aggregator.getSum();
			}
		};
		
		Collector <Object[],LongSummaryStatistics, Long> collector = Collector.of(initializer,aggregator,combiner,finalizer);
		
//		 Stream<Entry<List<Object>, Long>> result = 
			recordStream.collect
				(
						Collectors.groupingByConcurrent( 
							record -> 
								{
									List<Object> groupingKey = new ArrayList<>();
									for(int i=0; i<groupingSequenceIndexes.length; i++) {
										groupingKey.add(record[groupingSequenceIndexes[i]]);
									} 
									return groupingKey;
								}
						,collector
						)
				)
			.entrySet()
			.stream()
			.filter(
				(entry) -> entry.getValue() > 22
			)
			.forEach(
				(entry) -> System.out.println("Key : " +entry.getKey()+ "\t value : " + entry.getValue())
			);
			return null;
		 
	}


//	private class RecordAggregator{
//		
//		private long sum;
//		private int index;
//		
//		public RecordAggregator(int indexToAggregate) {
//			sum = 0;
//			index = indexToAggregate;
//		}
//		
//		public void aggregateRecord(Object[] record) {
//			sum += ((BigDecimal)record[index]).longValue();
//		}
//		
//		public long getSum() {
//			return sum;
//		}
//		
//	}

}
