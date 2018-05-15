package query.builder;

import java.util.ArrayList;
import java.util.List;



public class FilterClause  {
	
	private ArrayList<IFilterStatement> filterStatements;
	
	
	public FilterClause() {
		filterStatements = new ArrayList<>();
	}
	
	
	public void addStatement (IFilterStatement statement) {
		filterStatements.add(statement);
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		
		for(IFilterStatement statement : filterStatements) {
			sb.append(QueryConstants.WHITESPACE_CHAR);
			sb.append(statement.writeSql());
		}
		
		return sb.toString();
	}	
	
}
