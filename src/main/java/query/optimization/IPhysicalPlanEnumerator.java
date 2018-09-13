package query.optimization;

import java.util.Set;

import query.execution.ExecutionPlan;

public interface IPhysicalPlanEnumerator {

	Set<IPhysicalQueryPlan> enumeratePhysicalPlans(ILogicalQueryPlan logicalPlan);

}
