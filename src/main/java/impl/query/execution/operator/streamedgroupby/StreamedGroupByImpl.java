package impl.query.execution.operator.streamedgroupby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.CFNode;
import query.execution.operator.streamedgroupby.StreamedGroupByArgs;
import query.execution.operator.streamedgroupby.StreamedGroupByFunction;
import utils.CollectorUtils;
import utils.RecordAggregator;
import utils.RecordEvaluator;

public class StreamedGroupByImpl extends StreamedGroupByFunction {

	@Override
	public Stream<Object[]> apply(
			Stream<Object[]> inputStream, 
			Map<String, Integer> nameIndexMapping,
			StreamedGroupByArgs args) 
	{
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		List<AggregationDescriptor> aggregations = Lists.newArrayList(args.getAggregations());
		List<AggregateFilterStatement> aggregateFilters = args.getAggregateFIlters();
		List<IDescriptor> projectionSequence = args.getProjectionSequence();
		
		int[] groupingSequenceIndexes = 
				getGroupingSequenceIndexes(nameIndexMapping,groupingSequence);
		
		if(aggregations.size() == 0) {
			inputStream =
				groupRecords(inputStream, groupingSequenceIndexes);
		}else {
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector = 
				CollectorUtils.getRecordDownStreamCollector(
					aggregations,
					nameIndexMapping
				);
			inputStream =
				aggregateRecords(inputStream,groupingSequenceIndexes,downStreamCollector);
		}
		
		nameIndexMapping = getAggregateMapping(groupingSequence, aggregations);
		if(aggregateFilters.size() != 0) {
			inputStream = filterAggregateRecords(nameIndexMapping,aggregateFilters,inputStream);
		}
		return inputStream;
	}
	
	

	private int[] getGroupingSequenceIndexes(
			Map<String,Integer> nameIndexMapping, 
			List<FieldDescriptor> groupingSequence) 
	{
		int[] fieldSequenceIndexes = new int[groupingSequence.size()];
		int index = 0;
		for( FieldDescriptor field : groupingSequence) {
			fieldSequenceIndexes[index] = nameIndexMapping.get(field.getKey());
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
		Stream<Object[]> resultStream =
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


	private Map<String, Integer> getAggregateMapping(
			List<FieldDescriptor> groupingSequence,
			List<AggregationDescriptor> aggregations) 
	{
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
	
	
	private Stream<Object[]> filterAggregateRecords(
			Map<String,Integer> aggrNameIndexMapping,
			List<AggregateFilterStatement> aggregateFilters, 
			Stream<Object[]> aggregateStream)
	{
		Set<CFNode> statements = Sets.newHashSet(aggregateFilters);
		RecordEvaluator evaluator = new RecordEvaluator(aggrNameIndexMapping, statements);
		
		Stream<Object[]> filteredRecordStream =
		aggregateStream
		.filter(
			(Object[] record) -> evaluator.evaluate(record)
		);
		
		return filteredRecordStream;
	}

}
