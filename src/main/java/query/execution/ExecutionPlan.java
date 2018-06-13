package query.execution;

import utils.TreePrinter;


public class ExecutionPlan {
	
	private IExecutable rootExecutable;
	
	public ExecutionPlan(IExecutable rootExecutable) {
		this.rootExecutable = rootExecutable;
	}

	public IExecutable getRootExecutable() {
		return rootExecutable;
	}
	
	@Override
	public String toString() {
		TreePrinter tp = new TreePrinter();
		rootExecutable.addRepresentation(tp);
		return tp.toString();
	}
	
}
