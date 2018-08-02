package query.builder.clause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.ProjectionStatement;


public class ProjectionClause  {

	private LinkedList<ProjectionStatement> statements;
	private HashSet<FieldDescriptor> referencedFields;
 	private HashSet<AggregationDescriptor> aggregateFields;
	
	public ProjectionClause() {
		this.statements = new LinkedList();
		this.referencedFields = new HashSet<>();
		this.aggregateFields = new HashSet<>();
	}
	

	public void addStatement(ProjectionStatement statement) {
		statements.addLast(statement);
		IDescriptor field = statement.getField();
		if(field instanceof AggregationDescriptor) {
			AggregationDescriptor aggrField = AggregationDescriptor.class.cast(field);
			referencedFields.add(aggrField.getField());
			aggregateFields.add(aggrField);
		}
		else if (field instanceof FieldDescriptor) {
			referencedFields.add(FieldDescriptor.class.cast(field));
		}
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(QueryConstants.PROJECTION_CLAUSE);
		sb.append(QueryConstants.NEWLINE);
		
		Iterator<ProjectionStatement> it = statements.iterator();  
		ProjectionStatement currentStatement;
		while(it.hasNext()) {
			currentStatement = it.next();
			
			sb.append(currentStatement.getField().toString());
			
			if(it.hasNext()) {
				sb.append(QueryConstants.COMMA_CHAR);
				sb.append(QueryConstants.WHITESPACE_CHAR);
			}
		}
		
		return sb.toString();
	}
	
	
	public boolean referField(FieldDescriptor field) {
		return referencedFields.contains(field);
	}

	
	public List<ProjectionStatement> getStatements() {
		return statements;
	}
	
	
	public Set<FieldDescriptor> getReferencedFields(){
		return referencedFields;
	}
	
	
	public Set<AggregationDescriptor> getAggregateFields(){
		return aggregateFields;
	}
	
	
	public List<IDescriptor> getProjectionSequence(){
		List<IDescriptor> projectionSequence = new ArrayList<>();
		for(ProjectionStatement statement : statements) {
			if(statement.toSHow()) {
				projectionSequence.add(statement.getField());
			}
		}
		return projectionSequence;
	}
	
}
