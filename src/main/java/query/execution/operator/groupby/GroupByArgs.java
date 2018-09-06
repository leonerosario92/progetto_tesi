package query.execution.operator.groupby;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;
import query.builder.statement.AggregateFilterStatement;
import query.execution.operator.IOperatorArgs;

public class GroupByArgs implements IOperatorArgs {
	
	
	private List<FieldDescriptor> groupingSequence;
	private Set<AggregationDescriptor> aggregations;
	private List<AggregateFilterStatement> aggregateFilters;
	private List<FieldDescriptor> orderingSequence;
	private List<IDescriptor> projectionSequence;
	
	public GroupByArgs() {
		this.groupingSequence = new ArrayList<>(); 
		this.aggregations = new HashSet<>(); 
		this.aggregateFilters = new ArrayList<>();
		this.orderingSequence = new ArrayList<>();
		this.projectionSequence = new ArrayList<>();
	}
	
	
	public List<IDescriptor> getProjectionSequence() {
		return projectionSequence;
	}


	public void setProjectionSequence(List<IDescriptor> projectionSequence) {
		this.projectionSequence = projectionSequence;
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
	
	public List<FieldDescriptor> getOrderingSequence() {
		return orderingSequence;
	}
	
	public Set<IDescriptor> getColumns(){
		return Sets.newHashSet(orderingSequence);
	}

	public void setOrderingSequence(List<FieldDescriptor> orderingSequence) {
		this.orderingSequence.addAll(orderingSequence);
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

		sb.append(" , ");
		
		sb.append("orderingSequence = [ ");
		Iterator<FieldDescriptor> it = orderingSequence.iterator();
		while(it.hasNext()) {
			sb.append( it.next().getName());
			if(it.hasNext()) {
				sb.append(" , ");
			}
		}sb.append(" ]");
		
		return sb.toString();
	}


}
