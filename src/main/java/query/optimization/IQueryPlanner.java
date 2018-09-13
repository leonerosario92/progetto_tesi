package query.optimization;

import query.builder.Query;
import query.execution.ExecutionPlan;

public interface IQueryPlanner {

	public  ExecutionPlan getExecutionPlan(Query query) ;
	
}
