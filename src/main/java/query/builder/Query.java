package query.builder;


import java.util.HashSet;

import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.clause.FilterClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import query.builder.statement.SelectionStatement;

public class Query {

	private SelectionClause selectionClause;
	private ProjectionClause projectionClause;
	private FilterClause filterClause;
	
	Query() {
		this.selectionClause = new SelectionClause();
		this.projectionClause = new ProjectionClause();
		this.filterClause = new FilterClause();
	}
	
	
	public void select (SelectionStatement statement) {
		selectionClause.addSelectStatement(statement);
	}
	
	
	public void join(JoinStatement statement) {
		selectionClause.addJoinStatement(statement);
	}
	
	
	public void project (ProjectionStatement statement) {
		projectionClause.addStatement(statement);
	}
	
	
	public void filter (FilterStatement statement) {
		filterClause.addStatement(statement);
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(projectionClause.writeSql());
		sb.append(QueryConstants.NEWLINE);
		sb.append(selectionClause.writeSql());
		sb.append(QueryConstants.NEWLINE);
		sb.append(filterClause.writeSql());
		return sb.toString();
	}
	

	public boolean referTable(TableDescriptor table) {
		return selectionClause.referTable(table);
	}
	
	
	public boolean referField(FieldDescriptor field) {
		return projectionClause.referField(field);
	}
}