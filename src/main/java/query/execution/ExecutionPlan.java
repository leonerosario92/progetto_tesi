package query.execution;

import utils.TreePrinter;


public class ExecutionPlan {
	
	private OperatorGroup rootExecutable;
	
	public ExecutionPlan(OperatorGroup rootExecutable) {
		this.rootExecutable = rootExecutable;
	}

	public OperatorGroup getRootExecutable() {
		return rootExecutable;
	}
	
	@Override
	public String toString() {
		TreePrinter tp = new TreePrinter();
		rootExecutable.addRepresentation(tp);
		return tp.toString();
	}
	
}
