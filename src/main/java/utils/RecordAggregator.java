package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import dataset.IDataSet;
import datatype.TypeAggregator;
import model.FieldDescriptor;
import query.builder.predicate.AggregateFunction;
import query.builder.statement.AggregationDescriptor;

public class RecordAggregator {
	
	private List<AggregationDescriptor> aggregations;
	private List<TypeAggregator<?>> aggregators;
	private Map<String, Integer> mapping;
	
	
	public RecordAggregator(
		Set<AggregationDescriptor> aggregations, 
		IDataSet inputDataSet
	){
		this.aggregations = Lists.newArrayList(aggregations);
		this.mapping = inputDataSet.getNameIndexMapping();
		this.aggregators = new ArrayList<>();
		for(AggregationDescriptor aggregation : this.aggregations) {
			FieldDescriptor aggregationField = aggregation.getField();
			this.aggregators.add(aggregationField.getType().getDescriptor().getTypeAggregator().get());
		}
	}
	
	
	public void aggregateRecord(Object[] record) {
		int resultIndex = 0;
		for(AggregationDescriptor aggregation : aggregations) {
			int fieldIndex = mapping.get(aggregation.getField());
			aggregators.get(resultIndex).addValue(record[fieldIndex]);
		}
	}
	
	
	public Object[] getAggregationResult() {
		int resultSize = aggregations.size();
		Object[] result = new Object[resultSize];
		for( int i=0; i<resultSize; i++) {
			AggregateFunction aggrType = aggregations.get(i).getFunction();
			result[i] = aggregators.get(i).getSum();
		}
		return result;
	}
	
}
