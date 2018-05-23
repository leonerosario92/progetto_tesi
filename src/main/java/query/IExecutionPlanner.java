package query;

import query.builder.Query;
import query.execution.ExecutionPlanBlock;

public interface IExecutionPlanner {
	
	public ExecutionPlanBlock planExecution (Query query);
	
}