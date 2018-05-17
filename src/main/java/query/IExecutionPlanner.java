package query;

import query.builder.Query;
import query.execution.IExecutableSet;

public interface IExecutionPlanner {
	
	public IExecutableSet planExecution (Query query);
	
}