package query.optimization;

import query.execution.ExecutionPlan;

public interface IPhysicalPlanEvaluator {

	int computeCost(IPhysicalQueryPlan physicalPlan);

	
}
