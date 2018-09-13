package query.optimization;

import java.util.ArrayList;
import java.util.List;

import query.execution.ExecutionPlan;
import query.execution.operator.Operator;

public class PhysicalQueryPlan implements IPhysicalQueryPlan{
	
	private List<Operator<?,?>> operatorSequence;
	
	public PhysicalQueryPlan() {
		this.operatorSequence = new ArrayList<>();
	}

	@Override
	public int computeCost() {
//		IPhysicalOperator po = 
	}

	@Override
	public ExecutionPlan makeExecutable(ImplementationProvider provider) {
		// TODO Auto-generated method stub
		return null;
	}

}
