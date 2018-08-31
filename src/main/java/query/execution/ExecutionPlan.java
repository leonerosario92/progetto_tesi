package query.execution;

import dataset.IDataSet;
import query.execution.operator.IOperatorGroup;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;

public class ExecutionPlan {
	
	private IOperatorGroup<IDataSet> rootExecutable;
	
	public ExecutionPlan(IOperatorGroup<IDataSet> rootExecutable) {
		this.rootExecutable = rootExecutable;
	}

	public IOperatorGroup getRootExecutable() {
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
	
	public IExecutionReport getExecutionReport() {
		return rootExecutable.getReport();
	}
	
}
