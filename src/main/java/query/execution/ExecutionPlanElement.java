package query.execution;

import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;

public interface ExecutionPlanElement {
	
	public void addRepresentation (ExecutionPlanNavigator navigator);
	
	public void addRepresentationWithReport(ExecutionPlanNavigator navigator);

}
