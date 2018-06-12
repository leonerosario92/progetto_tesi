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
import query.builder.statement.FilterStatement;


public class FilterClause  {
	
	private ArrayList<FilterStatement> filterStatements;
	
	
	public FilterClause() {
		filterStatements = new ArrayList<>();
	}
	
	
	public void addStatement (FilterStatement statement) {
		filterStatements.add(statement);
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.FILTER_CLAUSE);
		
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
		return sb.toString();
	}
	
	
	public List<FilterStatement> getStatements(){
		return filterStatements;
	}
	
	
	public Set<FieldDescriptor> getReferencedFields(){
		HashSet<FieldDescriptor> result = new HashSet<>();
		for(FilterStatement statement : filterStatements) {
			result.add(statement.getField());
		}
		return result;
	}
	
	
}
