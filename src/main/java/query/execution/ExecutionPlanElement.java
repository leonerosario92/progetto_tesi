package query.execution;

import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorGroupReport;

public interface ExecutionPlanElement {
	
	public void addRepresentation (ExecutionPlanNavigator navigator);
	
	public void addRepresentationWithReport(ExecutionPlanNavigator navigator);
	
	public boolean generatesNewDataSet();
	
	public IExecutionReport getReport();

}
