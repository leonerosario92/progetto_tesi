package query.optimization;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

public class LogicalPlanEnumerator implements ILogicalPlanEnumerator {
	
	private Set<ITransformationRule> transformationRules;

	public LogicalPlanEnumerator(Set<ITransformationRule> transformationRules) {
		this.transformationRules = Sets.newHashSet(transformationRules);
	}

	@Override
	public Set<ILogicalQueryPlan> enumerateEquivalentPlans(ILogicalQueryPlan logicalPlan) {
		Set<ILogicalQueryPlan> equivalentPlans = new HashSet<>();
		for(ITransformationRule rule : transformationRules) {
			if(logicalPlan.testRuleApplicability(rule)) {
				ILogicalQueryPlan newPlan = logicalPlan.applyTransformationRule(rule);
				equivalentPlans.add(newPlan);
			}
		}
		return equivalentPlans;
	}

}

