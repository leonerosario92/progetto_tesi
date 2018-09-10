package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import dataset.IDataSet;
import dataset.IRecordMapper;
import datatype.AggregableTypeDescriptor;
import datatype.TypeAggregator;
import datatype.TypeDescriptor;
import model.AggregationDescriptor;
import model.FieldDescriptor;
import query.builder.predicate.AggregateFunction;

public class RecordAggregator {
	
	private List<AggregationDescriptor> aggregations;
	private List<TypeAggregator<?>> aggregators;
	private IRecordMapper recordMapper;
	
	public RecordAggregator(
			List<AggregationDescriptor> aggregations, 
			IRecordMapper recordMapper
		){
			this.aggregations = Lists.newArrayList(aggregations);
			this.recordMapper = recordMapper;
			this.aggregators = new ArrayList<>();
			for(AggregationDescriptor aggregation : this.aggregations) {
				TypeDescriptor fieldType =  aggregation.getField().getType().getDescriptor();
				if(fieldType instanceof AggregableTypeDescriptor) {
					this.aggregators.add(
							AggregableTypeDescriptor.class.cast(fieldType).getTypeAggregator()
					);
				}
			}
		}
	
	
	public void aggregateRecord(Object[] record) {
		int index = 0;
		for(AggregationDescriptor aggregation : aggregations) {
			int fieldIndex = recordMapper.getIndex(aggregation.getField());
			aggregators.get(index).addValue(record[fieldIndex]);
			index ++;
		}
	}
	
	
	public Object[] getAggregationResult() {
		int resultSize = aggregations.size();
		Object[] result = new Object[resultSize];
		for( int i=0; i<resultSize; i++) {
			AggregateFunction aggrType = aggregations.get(i).getFunction();
			result[i] = aggregators.get(i).getAggregationResult(aggrType);
		}
		return result;
	}


	public RecordAggregator combine(RecordAggregator other) {
		List<TypeAggregator<?>> otherAggregators = other.aggregators;
		Iterator<TypeAggregator<?>>otherIt = otherAggregators.iterator();
		Iterator<TypeAggregator<?>>thisIt = aggregators.iterator();
		while(thisIt.hasNext() && otherIt.hasNext()) {
			thisIt.next().combine(otherIt.next());
		}
		return this;
	}
	
}
