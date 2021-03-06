package query.execution.operator;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.execution.IReportableExecutable;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;
import query.optimization.ImplementationProvider;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorReport;

public abstract class SequentialOperator<F extends ISequentialOperatorFunction, A extends IOperatorArgs> 
extends Operator<F,A> implements IReportableExecutable,Executable<IDataSet>{

	protected OperatorReport report;
	protected boolean generatesNewDataSet;
	
	public abstract void setInputData(IDataSet...inputData);
	
	public SequentialOperator(ImplementationProvider provider, RelOperatorType type,boolean generatesNewDataSet) {
		this.report = new OperatorReport();
		this.generatesNewDataSet = generatesNewDataSet;
	}	
	
	@Override
	public boolean increaseMemoryOccupation() {
		return generatesNewDataSet;
	}
	
	
	@Override
	public void addRepresentation(ExecutableTreeNavigator printer) {
		printer.appendLine("[OPERATOR] ");
		printer.addIndentation();
		printer.appendLine("TYPE : " + operatorName);
		printer.appendLine("ARGS : { " + args.getStringRepresentation() + " }");
		printer.removeIndentation();
		printer.appendLine("[END OPERATOR]");
	}
	
	
	@Override
	public void addExecutionReport(ExecutableTreeNavigator printer ) {
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
