package query.execution.operator.groupby;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import model.FieldDescriptor;
import query.builder.predicate.AggregateFunctionType;
import query.builder.statement.AggregateFilterStatement;
import query.execution.ProcessDataSetOperator;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;

public class GroupByArgs implements IOperatorArgs {
	
	
	private List<FieldDescriptor> groupingSequence;
	private ListMultimap<AggregateFunctionType, AggregateFilterStatement> aggregateFilters;
	
	public GroupByArgs() {
		this.groupingSequence = new ArrayList<>(); 
		this.aggregateFilters = ArrayListMultimap.create();
	}
	
	
	public List<FieldDescriptor> getGroupingSequence() {
		return groupingSequence;
	}

	
	public void setGroupingSequence(List<FieldDescriptor> groupingSequence) {
		this.groupingSequence = groupingSequence;
	}

	
	public  ListMultimap<AggregateFunctionType, AggregateFilterStatement> getAggregateFilters() {
		return aggregateFilters;
	}

	
	public void addAggregateFilter(AggregateFunctionType function, AggregateFilterStatement statement) {
		aggregateFilters.put(function, statement);
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
		
		sb.append("Aggregate Filter Statements = [ ");
		int size = aggregateFilters.size();
		int count = 0;
		for(AggregateFilterStatement statement : aggregateFilters.values()) {
			sb.append(statement.writeSql());
			count ++;
			if(count < size) {
				sb.append(" , ");
			}
		}
		
		sb.append(" ]");
		
		return sb.toString();
	}


}
