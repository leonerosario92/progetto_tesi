package query.builder;


import java.math.BigDecimal;
import java.util.Date;
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
	
	private Long executionStartTime;
	private Long executionEndTime;
	private Long dataSetLoadingStartTime;
	private Long dataSetLoadingEndTime;
	private Long resultIterationStartTime;
	private Long resultIterationEndTime;
	
	private Long resultSetByteSize;
	
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
	
	
	public SelectionClause getSelectionClause() {
		return selectionClause;
	}


	public ProjectionClause getProjectionClause() {
		return projectionClause;
	}


	public FilterClause getFilterClause() {
		return filterClause;
	}


	public boolean referTable(TableDescriptor table) {
		return selectionClause.referTable(table);
	}
	
	
	public boolean referField(FieldDescriptor field) {
		return projectionClause.referField(field);
	}


	public void setExecutionStartTime() {
		this.executionStartTime = System.currentTimeMillis();
	}


	public void setExecutionEndTime() {
		this.executionEndTime = System.currentTimeMillis();
	}
	
	
	public void setDataSetLoadingStartTime() {
		this.dataSetLoadingStartTime = System.currentTimeMillis();
	}
	
	
	public void setDataSetLoadingEndTime() {
		this.dataSetLoadingEndTime = System.currentTimeMillis();
	}
	
	
	public void setResultIterationStartTime () {
		this.resultIterationStartTime = System.currentTimeMillis();
	}
	
	
	public void setResultIterationEndTime() {
		this.resultIterationEndTime = System.currentTimeMillis();
	}
	
	
	
	public long getExecutionTimeMillisecond() {
		if((executionStartTime == null) || (executionEndTime == null)) {
			throw new IllegalStateException("Execution time cannot be read if execution's start and end time are not explictly set");
		}
		return executionEndTime - executionStartTime;
	}
	
	
	public long getDataSetloadingTimeMillisecond() {
		if((dataSetLoadingStartTime == null) || (dataSetLoadingEndTime == null)) {
			throw new IllegalStateException("DataSet loading time cannot be read if execution's start and end time are not explictly set");
		}
		return dataSetLoadingEndTime - dataSetLoadingStartTime;
	} 
	

	public long getResultIterationTimeMillisecond() {
		if((resultIterationStartTime == null) || (resultIterationEndTime == null)) {
			throw new IllegalStateException("Result iteration time cannot be read if execution's start and end time are not explictly set");
		}
		return resultIterationEndTime - resultIterationStartTime;
	}
	

	public void setResultSetByteSize(Long resultSetByteSize) {
		this.resultSetByteSize = resultSetByteSize;
	}
	
	
	public Long getResultSetByteSize() {
		if(resultSetByteSize == null) {
			throw new IllegalStateException("Result set size cannot be retrieved since has not been explictly set");
		}
		return resultSetByteSize;
	}

}
	
	
