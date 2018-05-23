package query;

import query.builder.Query;
import query.execution.ExecutionPlanBlock;

public interface IQueryPlanner {

	public  ExecutionPlanBlock getExecutionPlan(Query query) ;
	
}
