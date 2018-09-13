package query.optimization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import query.execution.operator.Operator;

public class PhysicalPlanEnumerator implements IPhysicalPlanEnumerator {
	
	private ImplementationProvider implementationProvider;
	private OperatorRepository operatorRepository;

	public PhysicalPlanEnumerator(ImplementationProvider implementationProvider) {
		this.implementationProvider = implementationProvider;
	}

	@Override
	public Set<IPhysicalQueryPlan> enumeratePhysicalPlans(ILogicalQueryPlan logicalPlan) {
		
		List<IPhysicalQueryPlan> physicalPlans = new ArrayList<>();
		Set<ExecutionStrategy> strategies = operatorRepository.getAvailableStrategies();
		
		for(ExecutionStrategy strategy : strategies) {
			List<IPhysicalQueryPlan> plansForStrategy =
				getPlansForStrategy(strategy,logicalPlan);
//			for(ILogicalOperator<?> logOperator : logicalOperators) {
//				Set<Operator<?,?>> implementations = 
//					operatorRepository.getImplementations(strategy,logOperator.getLogicalType());
//				for(Operator<?,?> implementation : implementations) {
//					
//				}
//			}
		}
		
	}

	private List<IPhysicalQueryPlan> getPlansForStrategy(
			OperatorRepository repository,
			ExecutionStrategy strategy, 
			List<ILogicalOperator<?>> operators) 
	{
		Iterator<ILogicalOperator<?>> it = operators.iterator();
		
		while(it.hasNext()) {
			ILogicalOperator<?>currentOperator = it.next();
			Set<Operator<?,?>> implementations = 
					repository.getImplementations(strategy, currentOperator.getLogicalType());
			for(Operator<?,?> implementation : implementations) {
					
			}
		}
	}

}
