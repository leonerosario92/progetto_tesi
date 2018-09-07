package query.execution;

import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;

public interface IReportableExecutable {
	
	public void addRepresentation (ExecutableTreeNavigator navigator);
	
	public void addExecutionReport(ExecutableTreeNavigator navigator);
	
	public boolean increaseMemoryOccupation();
	
	public IExecutionReport getReport();

}
