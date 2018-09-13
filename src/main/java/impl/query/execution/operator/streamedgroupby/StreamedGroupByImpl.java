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

import dataset.ColumnDescriptor;
import dataset.IRecordMapper;
import datatype.DataType;
import impl.base.StreamedDataSet;
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
	public StreamedDataSet apply(
			StreamedDataSet pipeline, 
			StreamedGroupByArgs args) 
	{
		List<FieldDescriptor> groupingSequence = args.getGroupingSequence();
		List<AggregationDescriptor> aggregations = Lists.newArrayList(args.getAggregations());
		List<AggregateFilterStatement> aggregateFilters = args.getAggregateFIlters();
		List<IDescriptor> projectionSequence = args.getProjectionSequence();
		
		Stream<Object[]> recordStream = pipeline.getRecordStream();
		IRecordMapper recordMapper = pipeline.getRecordMapper();
		
		int[] groupingSequenceIndexes = 
				getGroupingSequenceIndexes(recordMapper,groupingSequence);
		
		if(aggregations.size() == 0) {
			recordStream =
				groupRecords(recordStream, groupingSequenceIndexes);
		}else {
			Collector<Object[], RecordAggregator, Object[]> downStreamCollector = 
				CollectorUtils.getRecordDownStreamCollector(
					aggregations,
					recordMapper
				);
			recordStream =
				aggregateRecords(recordStream,groupingSequenceIndexes,downStreamCollector);
		}
		
		List<ColumnDescriptor> newColumnSequence = 
				getUpdatedColumnSequence(groupingSequence,aggregations);
		pipeline.updateColumnDescriptors(newColumnSequence);
		Map<String,Integer> updatedNameIndexMapping = getUpdatedMapping(groupingSequence,aggregations);
		
		if(aggregateFilters.size() != 0) {
			recordStream = filterAggregateRecords(updatedNameIndexMapping,aggregateFilters,recordStream);
		}
		
		pipeline.updateStream(recordStream);
		System.out.println("");
		return pipeline;
		
	}
	

	private List<ColumnDescriptor> getUpdatedColumnSequence(
			List<FieldDescriptor> groupingSequence,
			List<AggregationDescriptor> aggregations)
	{
		List<ColumnDescriptor> result = new ArrayList<>();
		int index = 0;
		for(FieldDescriptor field : groupingSequence) {
			String tableName = field.getTable().getName();
			String columnName = field.getName();
			DataType columnType = field.getType();
			ColumnDescriptor column = new ColumnDescriptor(tableName, columnName, columnType);
			result.add(index,column);
			index++;
		}
		for(AggregationDescriptor aggregation : aggregations) {
			String tableName = aggregation.getTable().getName();
			String columnName = aggregation.getName();
			DataType columnType = DataType.DOUBLE;
			ColumnDescriptor column = new ColumnDescriptor(tableName, columnName, columnType);
			result.add(index,column);
			index ++;
		}
		return result;
	}

	
	private int[] getGroupingSequenceIndexes(
			IRecordMapper recordMapper,
			List<FieldDescriptor> groupingSequence)
	{
		int[] fieldSequenceIndexes = new int[groupingSequence.size()];
		int index = 0;
		for( FieldDescriptor field : groupingSequence) {
			fieldSequenceIndexes[index] = recordMapper.getIndex(field.getKey());
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
		Map<List<Object>, Object[]> map =
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
		
		Stream<Object[]> resultStream =
		map.entrySet()
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


	private Map<String, Integer> getUpdatedMapping(
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
