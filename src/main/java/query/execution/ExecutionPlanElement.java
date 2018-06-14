package query.execution;

import utils.TreePrinter;
import utils.report.ExecutionReport;

public interface ExecutionPlanElement {
	
	public void addRepresentation (TreePrinter printer);
	
	public void addRepresentationWithReport(TreePrinter printer);

}
