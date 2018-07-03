package query.builder.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;


public class FilterClause  {
	
	private ArrayList<FilterStatement> filterStatements;
	private ArrayList<CFilterStatement> composedFilterStatements;
	
	
	public FilterClause() {
		filterStatements = new ArrayList<>();
		composedFilterStatements = new ArrayList<>();
	}
	
	
	public void addStatement (FilterStatement statement) {
		filterStatements.add(statement);
	}
	
	
	public void addStatement (CFilterStatement statement) {
		composedFilterStatements.add(statement);
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.FILTER_CLAUSE);
		appendFilterStatements(sb);
		if( (!(filterStatements.isEmpty()) &&  (!(composedFilterStatements.isEmpty())) )) {
			sb.append(QueryConstants.WHITESPACE_CHAR);
			sb.append(QueryConstants.AND);
			sb.append(QueryConstants.WHITESPACE_CHAR);
		}
		appendCFilterStatements(sb);
		return sb.toString();
	}
	
	
	private void appendFilterStatements(StringBuilder sb) {
		Iterator<FilterStatement> it = filterStatements.iterator();
		FilterStatement statement;
		while(it.hasNext()) {
			statement = it.next();
			sb.append(QueryConstants.WHITESPACE_CHAR);
			sb.append(statement.writeSql());
			if(it.hasNext()) {
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(QueryConstants.AND);
				sb.append(QueryConstants.WHITESPACE_CHAR);
			}
		}
	}


	private void appendCFilterStatements(StringBuilder sb) {
		
		Iterator<CFilterStatement> it = composedFilterStatements.iterator();
		CFilterStatement statement;

		while(it.hasNext()) {
			statement = it.next();
			sb.append(statement.writeSql());
			if(it.hasNext()) {
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(QueryConstants.AND);
				sb.append(QueryConstants.WHITESPACE_CHAR);
			}
		}
	}


	public List<FilterStatement> getStatements(){
		return filterStatements;
	}
	
	
	public List<CFilterStatement> getComposedStatements(){
		return composedFilterStatements;
	}
	
	
	public Set<FieldDescriptor> getReferencedFields(){
		HashSet<FieldDescriptor> result = new HashSet<>();
		for(FilterStatement statement : filterStatements) {
			result.add(statement.getField());
		}
		for(CFilterStatement statement : composedFilterStatements) {
			result.addAll(statement.getReferencedFields());
		}
		return result;
	}
	
	
}
