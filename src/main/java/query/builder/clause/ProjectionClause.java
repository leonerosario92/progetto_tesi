package query.builder.clause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import model.FieldDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.ProjectionStatement;


public class ProjectionClause  {

	private ArrayList<ProjectionStatement> statements;
	private HashSet<String> referencedFields;
	
	public ProjectionClause() {
		this.statements = new ArrayList<>();
		referencedFields = new HashSet<>();
	}

	public void addStatement(ProjectionStatement statement) {
		statements.add(statement);
		FieldDescriptor field = statement.getField();
		String key = field.getName()+"_"+field.getTable().getName();
		referencedFields.add(key);
	}
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(QueryConstants.PROJECTION_CLAUSE);
		sb.append(QueryConstants.NEWLINE);
		
		Iterator<ProjectionStatement> it = statements.iterator();  
		ProjectionStatement currentStatement;
		while(it.hasNext()) {
			currentStatement = it.next();
			
			sb.append(currentStatement.getField().getTable().getName());
			sb.append(QueryConstants.DOT_CHAR);
			sb.append(currentStatement.getField().getName());
			
			if(it.hasNext()) {
				sb.append(QueryConstants.COMMA_CHAR);
				sb.append(QueryConstants.WHITESPACE_CHAR);
			}
		}
		
		return sb.toString();
	}
	
	
	public boolean referField(FieldDescriptor field) {
		String key = field.getName()+"_"+field.getTable().getName();
		return referencedFields.contains(key);
	}
	
}
