package query.builder.clause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.AggregateFilterStatement;

public class GroupByClause {

	private List<FieldDescriptor> groupingSequence;
	private List<AggregateFilterStatement> aggregateFilters;

	
	public GroupByClause(FieldDescriptor...fields) {
		groupingSequence = new ArrayList<>();
		aggregateFilters = new ArrayList<>();
	}
	
	
	public void addStatement(FieldDescriptor...fields) {
		for(int i=0; i<fields.length; i++) {
			groupingSequence.add(i, fields[i]);
		}
	}
	
	
	public List<FieldDescriptor> getGroupingSequence(){
		return groupingSequence;
	}

	
	public Set<FieldDescriptor> getReferencedFields() {
		HashSet<FieldDescriptor> referencedFields = new HashSet<>();
		for(FieldDescriptor field : groupingSequence) {
			referencedFields.add(field);
		}
		return referencedFields;
	}
	
	
	public void addAggregateFilter(AggregateFilterStatement statement) {
		aggregateFilters.add(statement);
	}
	
	
	public  List<AggregateFilterStatement> getAggregateFilterStatements() {
		return aggregateFilters;
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.GROUP_BY_CLAUSE);
		sb.append(QueryConstants.WHITESPACE_CHAR);
		
		Iterator<FieldDescriptor> it = groupingSequence.iterator();
		FieldDescriptor field;
		if(it.hasNext()) {
			field = it.next();
			sb.append(field.getName());
		}
		while (it.hasNext()) {
			field = it.next();
			sb.append(QueryConstants.WHITESPACE_CHAR)
			.append(QueryConstants.COMMA_CHAR)
			.append(QueryConstants.WHITESPACE_CHAR)
			.append(field.getName());
		}
		
		if(! aggregateFilters.isEmpty()) {
			sb.append(QueryConstants.NEWLINE)
			.append(QueryConstants.AGGREGATE_FILTER_CLAUSE)
			.append(QueryConstants.WHITESPACE_CHAR);
			Iterator<AggregateFilterStatement> iterator = 
					aggregateFilters.iterator();
			if(iterator.hasNext()) {
				AggregateFilterStatement next = iterator.next();
				sb.append(next.writeSql());
			}
			while(iterator.hasNext()) {
				AggregateFilterStatement next = iterator.next();
				sb.append(QueryConstants.WHITESPACE_CHAR)
				.append(QueryConstants.AND)
				.append(QueryConstants.WHITESPACE_CHAR)
				.append(next.writeSql());
			}
		}

		return sb.toString();
	}

}
