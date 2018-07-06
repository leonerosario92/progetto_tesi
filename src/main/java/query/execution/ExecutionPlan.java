package query.execution;

import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;

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
		ExecutionPlanNavigator navigator = new ExecutionPlanNavigator();
		rootExecutable.addRepresentation(navigator);
		return navigator.toString();
	}
	
	public String printReport() {
		ExecutionPlanNavigator navigator = new ExecutionPlanNavigator();
		rootExecutable.addRepresentationWithReport(navigator);
		return navigator.toString();
	}
	
	public ExecutionReport getExecutionReport() {
		return rootExecutable.getReport();
	}
	
}
