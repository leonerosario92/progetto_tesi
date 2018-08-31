package impl.base;

import dataset.IDataSet;
import query.QueryPlanner;
import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.GroupByClause;
import query.builder.clause.OrderByClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.execution.ExecutionPlan;
import query.execution.operator.IOperatorGroup;
import query.execution.operator.StreamOperatorGroup;
import query.execution.operator.loadstream.LoadStreamArgs;
import query.execution.operator.loadstream.LoadStreamOperator;

public class StreamedQueryPlanner extends QueryPlanner{
	
	public StreamedQueryPlanner() {
		super();
	}

	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		SelectionClause selectionClause = query.getSelectionClause();
		ProjectionClause projectionClause = query.getProjectionClause();
		FilterClause filterClause = query.getFilterClause();
		GroupByClause groupByClause = query.getGroupByClause();
		OrderByClause orderByClause = query.getOrderByClause();
		
		LoadStreamOperator dataLoadingOperator = 
			new LoadStreamOperator(implementationProvider);
		LoadStreamArgs dataLoadingArgs = dataLoadingOperator.getArgs();
		dataLoadingArgs.setColumns(projectionClause.getReferencedFields());
		
		IOperatorGroup<IDataSet> rootExecutable = new StreamOperatorGroup(dataLoadingOperator);
		ExecutionPlan execPlan = new ExecutionPlan(rootExecutable);
		return execPlan;
	}
	
	

}
