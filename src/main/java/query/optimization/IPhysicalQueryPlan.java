package query.optimization;

import query.execution.ExecutionPlan;

public interface IPhysicalQueryPlan {

	public int computeCost();
	
	public ExecutionPlan makeExecutable(ImplementationProvider provider);
	
}
