package impl.query.execution.operator.groupBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Lists;

import dataset.ColumnDescriptor;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import model.FieldDescriptor;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;

public class GroupByImpl  extends GroupByFunction{

	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {
		
		Stream<Object[]> recordStream = inputDataSet.getRecordStream();
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		
		int[] fieldSequenceIndexes = 
				getGroupingSequenceIndexes(inputDataSet,groupingSequence);
		
		Map<Object, List<Object[]>> groupedRecords =
				groupRecords(recordStream, fieldSequenceIndexes);
		
		
		int fieldToAggregate = inputDataSet.getColumnIndex(
				args.getAggregateFilters().values().iterator().next().getField()
		);
		
		
		
		
		List<Object[]> aggregateRecords =
			groupedRecords.entrySet().stream()
			.map( 
				entry -> {
						Object[] aggregateRecord = new Object[2];
						aggregateRecord[0] = entry.getKey();
						
						RecordAggregator aggregator = new RecordAggregator(fieldToAggregate);
						entry.getValue().forEach(
							record -> {
								aggregator.aggregateRecord(record);
							}
						);
						aggregateRecord[1] = aggregator.getSum();
						return aggregateRecord;
				}
			)
			.filter(record ->  {  return((long)record[1] > 22);  })
			.collect(Collectors.toList());

		
		
		List<ColumnDescriptor> columnSequence = new ArrayList<>();
		int newRecordCount = aggregateRecords.size();
		columnSequence.add(inputDataSet.getColumnDescriptor(fieldToAggregate));
//		columnSequence.add(inputDataSet.getColumnDescriptor(index));
		
		
		
		IDataSet result = layoutManager.buildDataSet(newRecordCount, columnSequence, aggregateRecords.iterator());
		
		System.out.println("Result : ");
		aggregateRecords.stream()
		.forEach( 
				record ->{
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<2; i++) {
						sb.append("\t\t"+record[i]);
					}
					
					System.out.println(sb.toString());
				}
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
	
	
	private Map<Object, List<Object[]>> groupRecords(Stream<Object[]> recordStream, int[] groupingSequenceIndexes) {
		Map<Object, List<Object[]>> groupedRecords = recordStream.collect
				(
					Collectors.groupingByConcurrent( 
						record -> 
							{
								List<Object> groupingKey = Lists.newArrayList();
								for(int i=0; i<groupingSequenceIndexes.length; i++) {
									groupingKey.add(record[groupingSequenceIndexes[i]]);
								} 
								return groupingKey;
							}
					)
				);
		return groupedRecords;
	}


	private class RecordAggregator{
		
		private long sum;
		private int index;
		
		public RecordAggregator(int indexToAggregate) {
			sum = 0;
			index = indexToAggregate;
		}
		
		public void aggregateRecord(Object[] record) {
			sum += ((BigDecimal)record[index]).longValue();
		}
		
		public long getSum() {
			return sum;
		}
		
	}

}
