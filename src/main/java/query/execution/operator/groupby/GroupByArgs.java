package query.execution.operator.groupby;

import java.text.MessageFormat.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import model.FieldDescriptor;
import query.builder.predicate.AggregateFunction;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.AggregationDescriptor;
import query.execution.ProcessDataSetOperator;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;

public class GroupByArgs implements IOperatorArgs {
	
	
	private List<FieldDescriptor> groupingSequence;
	private Set<AggregationDescriptor> aggregations;
	private List<AggregateFilterStatement> aggregateFilters;
	
	public GroupByArgs() {
		this.groupingSequence = new ArrayList<>(); 
		this.aggregations = new HashSet<>(); 
		this.aggregateFilters = new ArrayList<>();
	}
	
	
	public List<FieldDescriptor> getGroupingSequence() {
		return groupingSequence;
	}
	
	public void setGroupingSequence(List<FieldDescriptor> groupingSequence) {
		this.groupingSequence = groupingSequence;
	}
	
	
	public  void addAggregation(AggregationDescriptor aggregation) {
		aggregations.add(aggregation);
	}
	
	public Set<AggregationDescriptor> getAggregations () {
		return aggregations;
	}
	
	
	public void addAggregateFilter (AggregateFilterStatement statement) {
		AggregationDescriptor descriptor = statement.getAggregationDescriptor();
		aggregations.add(descriptor);
		aggregateFilters.add(statement);
	}
	
	public List<AggregateFilterStatement> getAggregateFIlters(){
		return aggregateFilters;
	}
	
	


	@Override
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("groupingSequence = [ ");
		Iterator<FieldDescriptor> seqIterator = groupingSequence.iterator();
		while(seqIterator.hasNext()) {
			sb.append( seqIterator.next().getName());
			if(seqIterator.hasNext()) {
				sb.append(" , ");
			}
		}sb.append(" ]");
		
		sb.append(" , ");
		
		if(aggregateFilters.size() != 0) {
			sb.append("aggregateFilterStatements = [ ");
			int size = aggregateFilters.size();
			int count = 0;
			for(AggregateFilterStatement statement : aggregateFilters) {
				sb.append(statement.writeSql());
				count ++;
				if(count < size) {
					sb.append(" , ");
				}
			}
			sb.append(" ]");
		}
		
		
		return sb.toString();
	}


}
