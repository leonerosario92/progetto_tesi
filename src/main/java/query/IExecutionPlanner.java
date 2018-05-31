package query;

import query.builder.Query;
import query.execution.ExecutionPlan;

public interface IExecutionPlanner {
	
	public ExecutionPlan planExecution (Query query);
	
}