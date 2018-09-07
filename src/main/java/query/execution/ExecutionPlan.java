package query.execution;

import dataset.IDataSet;
import query.execution.operator.IOperatorGroup;
import utils.ExecutableTreeNavigator;
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
		ExecutableTreeNavigator navigator = new ExecutableTreeNavigator();
		rootExecutable.addRepresentation(navigator);
		return navigator.toString();
	}
	
	public String printReport() {
		ExecutableTreeNavigator navigator = new ExecutableTreeNavigator();
		rootExecutable.addExecutionReport(navigator);
		return navigator.toString();
	}
	
	public IExecutionReport getExecutionReport() {
		return rootExecutable.getReport();
	}
	
}
