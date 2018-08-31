package query.execution.operator;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.ImplementationProvider;
import query.execution.ReportablePlanElement;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorReport;

public abstract class SequentialOperator<F extends ISequentialOperatorFunction, A extends IOperatorArgs> 
extends Operator<F,A> implements ReportablePlanElement,ExecutableUnit<IDataSet>{

	protected OperatorReport report;
	protected boolean generatesNewDataSet;
	
	public abstract void setInputData(IDataSet...inputData);
	
	public SequentialOperator(ImplementationProvider provider, RelOperatorType type,boolean generatesNewDataSet) {
		this.report = new OperatorReport();
		this.generatesNewDataSet = generatesNewDataSet;
	}	
	
	@Override
	public boolean generatesNewDataSet() {
		return generatesNewDataSet;
	}
	
	
	@Override
	public void addRepresentation(ExecutionPlanNavigator printer) {
		printer.appendLine("[OPERATOR] ");
		printer.addIndentation();
		printer.appendLine("TYPE : " + operatorName);
		printer.appendLine("ARGS : { " + args.getStringRepresentation() + " }");
		printer.removeIndentation();
		printer.appendLine("[END OPERATOR]");
	}
	
	
	@Override
	public void addRepresentationWithReport(ExecutionPlanNavigator printer ) {
		printer.appendLine("[OPERATOR " + report.toString() + "]");
		printer.addIndentation();
		printer.appendLine("TYPE : " + operatorName);
		printer.appendLine("ARGS : { " + args.getStringRepresentation() + " }");
		printer.removeIndentation();
		printer.appendLine("[END OPERATOR]");
	}
	
	
	@Override
	public IExecutionReport getReport() {
		return report;
	}
	
	
	@Override
	public  IDataSet execute(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException{
		report.setExecutionStartTime();
		IDataSet result = execute(executor);
		report.setExecutionEndTime();
		if(measurement.equals(MeasurementType.EVALUATE_MEMORY_OCCUPATION)) {
			if(this.generatesNewDataSet) {
				long memoryOccupation = MemoryMeasurer.measureBytes(result);
				report.setMemoryOccupationByte(memoryOccupation);
			}
		}
		return result;
	}
	
}
