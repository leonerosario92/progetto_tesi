package query.optimization;

import java.util.Set;

public interface ILogicalPlanEnumerator {

	Set<ILogicalQueryPlan> enumerateEquivalentPlans(ILogicalQueryPlan logicalPlan);

}

