package impl.query.execution.operator.groupBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.CFNode;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByFunction;
import utils.CollectorUtils;
import utils.RecordAggregator;
import utils.RecordComparator;
import utils.RecordEvaluator;

public class GroupByImpl  extends GroupByFunction{
	
	@Override
	public IDataSet apply(IDataSet inputDataSet, ILayoutManager layoutManager, GroupByArgs args) {

		Stream<Object[]> recordStream = inputDataSet.getRecordStream();
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		List<AggregationDescriptor> aggregations = Lists.newArrayList(args.getAggregations());
		List<AggregateFilterStatement> aggregateFilters = args.getAggregateFIlters();
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		List<IDescriptor> projectionSequence = args.getProjectionSequence();
		
		int[] groupingSequenceIndexes = 
				getGroupingSequenceIndexes(inputDataSet,groupingSequence);
		
		List<Object[]> aggregateRecords;
		if(aggregations.size() == 0) {
			recordStream =
					groupRecords(recordStream, groupingSequenceIndexes);
		}else {
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector = 
				CollectorUtils.getRecordDownStreamCollector(
					aggregations,
					inputDataSet.getNameIndexMapping()
				);
			recordStream =
					aggregateRecords(recordStream,groupingSequenceIndexes,downStreamCollector);
		}
		
		Map<String,Integer> nameIndexMapping = mapAggregateRecordsToIndexes(groupingSequence, aggregations);
		
		if(aggregateFilters.size() != 0) {
			recordStream = 
				filterRecords(nameIndexMapping,aggregateFilters,recordStream);
		}
		
		recordStream = projectFields(projectionSequence,recordStream,nameIndexMapping);
		
		if(orderingSequence.size() !=0) {
			recordStream =
				sortRecords(nameIndexMapping,orderingSequence,recordStream);
		}
		
		aggregateRecords = 
				recordStream
				.collect(Collectors.toList());
		
		List<ColumnDescriptor> columnSequence = getResultColumnSequence(projectionSequence);
		IDataSet result = layoutManager.buildMaterializedDataSet(
				aggregateRecords.size(),
				columnSequence,
				aggregateRecords.iterator()
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
	
	
	private Stream<Object[]> groupRecords(Stream<Object[]> recordStream, int[] fieldSequenceIndexes) {
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
		return result.stream();
	}
	
	
	
	private Stream<Object[]> aggregateRecords(
			Stream<Object[]> recordStream, 
			int[] fieldSequenceIndexes,
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector
	){
		Stream<Object[]> resultStream=
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
		)
		.entrySet()
		.stream()
		.map(
			entry -> {
				List<Object> key = entry.getKey();
				Object[] value = entry.getValue();
				Object[] currentRecord = new Object[key.size() + value.length];
				for(int i=0; i<key.size(); i++) {
					currentRecord[i] = key.get(i);
				}
				for(int i=key.size(); i < (key.size() + value.length); i++) {
					currentRecord[i] = value[ i-key.size()];
				}
				return currentRecord;
			}
		);
		
		return resultStream;
	}


	
	private Map<String, Integer> mapAggregateRecordsToIndexes
		(List<FieldDescriptor> groupingSequence,List<AggregationDescriptor> aggregations) {
			int groupingSequenceSize = groupingSequence.size();
			Map<String,Integer> mapping = new HashMap<>();
			
			int index = 0;
			for(FieldDescriptor field : groupingSequence) {
				mapping.put(field.getKey(), index);
				index++;
			}
			for(AggregationDescriptor aggregation : aggregations) {
				mapping.put(aggregation.getKey(), index);
				index ++;
			}
			
			return mapping;
	}
	

	private Stream<Object[]> filterRecords(
			Map<String,Integer> aggrNameIndexMapping,
			List<AggregateFilterStatement> aggregateFilters, 
			Stream<Object[]> aggregateStream
	){
		Set<CFNode> statements = Sets.newHashSet(aggregateFilters);
		RecordEvaluator evaluator = new RecordEvaluator(aggrNameIndexMapping, statements);
		
		Stream<Object[]> filteredRecordStream =
		aggregateStream
		.filter(
			(Object[] record) -> evaluator.evaluate(record)
		);
		
		return filteredRecordStream;
	}
	
	
	private List<ColumnDescriptor> getResultColumnSequence(
		List<IDescriptor> projectionSequence)
	{
		List<ColumnDescriptor> sequence = new ArrayList<>();
		for(IDescriptor descriptor : projectionSequence) {
			if(descriptor instanceof FieldDescriptor) {
				FieldDescriptor field = FieldDescriptor.class.cast(descriptor);
				ColumnDescriptor currentColumn = new ColumnDescriptor(
						field.getTable().getName(), 
						field.getName(), 
						field.getType()
				);
				sequence.add(currentColumn);
			}
			else if(descriptor instanceof AggregationDescriptor) {
				AggregationDescriptor aggregateField = AggregationDescriptor.class.cast(descriptor);
				ColumnDescriptor currentColumn = new ColumnDescriptor(
						aggregateField.getField().getTable().getName(), 
						aggregateField.getName(), 
						DataType.DOUBLE
				);
				sequence.add(currentColumn);
			}
		}
		return sequence;
	}
	
	
	private Stream<Object[]> sortRecords(
			Map<String, Integer> aggrNameIndexMapping,
			List<FieldDescriptor> orderingSequence, 
			Stream<Object[]> aggregateRecordStream
	){
		Comparator<Object[]> recordComparator = 
				RecordComparator.getRecordComparator(aggrNameIndexMapping, orderingSequence);
		Stream<Object[]> sortedRecordStream =
			aggregateRecordStream
			.sorted(recordComparator);
		return sortedRecordStream;
	}
	
	
	private Stream<Object[]> projectFields(
			List<IDescriptor> projectionSequence, 
			Stream<Object[]> recordStream,
			Map<String, Integer> oldMapping
	){
		Map<String,Integer> newMapping = new HashMap<>();
		
		int index = 0;
		for(IDescriptor field : projectionSequence) {
			newMapping.put(field.getKey(),index);
			index++;
		}
		
		Stream<Object[]> result =
		recordStream.map(
			oldRecord ->{
				Object[] newRecord = new Object[projectionSequence.size()];
				int j = 0;
				for(IDescriptor field : projectionSequence) {
					newRecord[j] = oldRecord[oldMapping.get(field.getKey())];
					j++;
				}
				return newRecord;
 			}
		);
		return result;
	}

}