package query.optimization;

import java.util.Set;

import query.builder.Query;
import query.execution.ExecutionPlan;

public class DynamicQueryPlanner extends QueryPlanner{

	private Set<ITransformationRule> transformationRules;
	private ILogicalPlanEnumerator logPlanEnumerator;
	private IPhysicalPlanEnumerator exPlanEnumerator;

	
	public DynamicQueryPlanner() {
		this.logPlanEnumerator = new LogicalPlanEnumerator(transformationRules);
		this.exPlanEnumerator = new PhysicalPlanEnumerator(implementationProvider);
	
	}
	
	
	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		ILogicalQueryPlan initialPlan = new LogicalQueryPlan();
		initialPlan.initializeFromQuery(query);
		
		Set<ILogicalQueryPlan> logicalPlans =
			logPlanEnumerator.enumerateEquivalentPlans(initialPlan);
		
		int lowerCost = Integer.MAX_VALUE;
		IPhysicalQueryPlan bestPlan = null;
		for(ILogicalQueryPlan logicalPlan : logicalPlans) {
			Set<IPhysicalQueryPlan> executionPlans =
				exPlanEnumerator.enumeratePhysicalPlans(logicalPlan);
			for(IPhysicalQueryPlan currentPlan : executionPlans) {
				int currentCost = currentPlan.computeCost();
				if(currentCost < lowerCost) {
					lowerCost = currentCost;
					bestPlan = currentPlan;
				}
			}
		}
		
		return bestPlan.makeExecutable(implementationProvider);
		
	}

}
