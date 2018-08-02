package query.builder;


import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.clause.FilterClause;
import query.builder.clause.GroupByClause;
import query.builder.clause.OrderByClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import query.builder.statement.SelectionStatement;

public class Query {

	private SelectionClause selectionClause;
	private ProjectionClause projectionClause;
	private FilterClause filterClause;
	private OrderByClause orderByClause;
	private GroupByClause groupByClause;
	
	private float executionTime;
	private float resultIterationTime;
	private float memoryOccupation;
	private String executionReport;
	
	
	Query() {
		this.selectionClause = new SelectionClause();
		this.projectionClause = new ProjectionClause();
		this.filterClause = new FilterClause();
		this.orderByClause = new OrderByClause();
		this.groupByClause = new GroupByClause();
		
		this.executionTime = this.resultIterationTime = this.memoryOccupation = 0;
		this.executionReport = "";
	}
	
	
	public void select (SelectionStatement statement) {
		selectionClause.addStatement(statement);
	}
	
	
	public void project (ProjectionStatement statement) {
		projectionClause.addStatement(statement);
	}
	
	
	public void filter (FilterStatement statement) {
		filterClause.addStatement(statement);
	}
	
	
	public void filter(CFilterStatement composedStatement) {
		filterClause.addStatement(composedStatement);
	}
	
	
	public void orderBy(FieldDescriptor...fields) {
		orderByClause.addStatement(fields);
	}
	
	public void groupBy(FieldDescriptor...fields) {
		groupByClause.addStatement(fields);
	}
	
	public void aggregateFilter(AggregateFilterStatement statement) {
		groupByClause.addAggregateFilter(statement);
		projectionClause.addStatement(new ProjectionStatement(statement.getAggregationDescriptor(), false));
	}
	
	public SelectionClause getSelectionClause() {
		return selectionClause;
	}

	public ProjectionClause getProjectionClause() {
		return projectionClause;
	}

	public FilterClause getFilterClause() {
		return filterClause;
	}
	
	public OrderByClause getOrderByClause() {
		return orderByClause;
	}
	
	public GroupByClause getGroupByClause() {
		return groupByClause;
	}


	public boolean referTable(TableDescriptor table) {
		return selectionClause.referTable(table);
	}
	
	
	public boolean referField(FieldDescriptor field) {
		return projectionClause.referField(field);
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(projectionClause.writeSql());
		sb.append(QueryConstants.NEWLINE);
		sb.append(selectionClause.writeSql());
		
		if( ! ( 
				(filterClause.getStatements().isEmpty()) && 
				(filterClause.getComposedStatements().isEmpty())
			) ) {
			sb.append(QueryConstants.NEWLINE);
			sb.append(filterClause.writeSql());
		}
		
		if( ! (groupByClause.getGroupingSequence().isEmpty())) {
			sb.append(QueryConstants.NEWLINE);
			sb.append(groupByClause.writeSql());
		}
		
		if( ! (orderByClause.getOrderingSequence().isEmpty())) {
			sb.append(QueryConstants.NEWLINE);
			sb.append(orderByClause.writeSql());
		}
		
		return sb.toString();
	}


	public void setExecutionTime(float executionTime) {
		this.executionTime = executionTime;
	}
	
	public float getExecutionTime() {
		return this.executionTime;
	}
	
	public void setMemoryOccupation(float memoryOccupation) {
		this.memoryOccupation = memoryOccupation;
	}
	
	public float getMemoryOccupation() {
		return this.memoryOccupation;
	}
	
	public void setResultIterationTime( float resultIterationTime) {
		this.resultIterationTime = resultIterationTime;
	}
	
	public float getResultIterationTime() {
		return this.resultIterationTime;
	}
	
	public void setExecutionReport(String executionReport) {
		this.executionReport = executionReport;
	}
	
	public String getExecutionReport() {
		return this.executionReport;
	}



}
	
	
