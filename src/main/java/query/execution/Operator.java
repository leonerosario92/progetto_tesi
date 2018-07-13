package query.execution;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.ImplementationProvider;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.RelOperatorType;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorReport;


public abstract class Operator<F extends IOperatorFunction, A extends IOperatorArgs> implements ExecutionPlanElement{
	
	protected A args;
	protected F function;
	protected String operatorName;
	protected OperatorReport report;
	protected boolean generatesNewDataSet;
	
	
	public Operator(ImplementationProvider provider, RelOperatorType type,boolean generatesNewDataSet) {
		this.report = new OperatorReport();
		this.generatesNewDataSet = generatesNewDataSet;
	}	
	
	
	public A getArgs() {
		return args;
	}
	
	@Override
	public IExecutionReport getReport() {
		return report;
	}
	
	
	public  abstract void setInputData(IDataSet...inputData);
	
	
	public abstract IDataSet execOperator(IQueryExecutor executor) throws QueryExecutionException;
	
	
	public  IDataSet execOperator(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException{
		report.setExecutionStartTime();
		IDataSet result = execOperator(executor);
		report.setExecutionEndTime();
		if(measurement.equals(MeasurementType.EVALUATE_MEMORY_OCCUPATION)) {
			if(this.generatesNewDataSet) {
				long memoryOccupation = MemoryMeasurer.measureBytes(result);
				report.setMemoryOccupationByte(memoryOccupation);
//				report.setMemoryOccupationByte(20 *1024 *1024);
			}
		}
		return result;
	}
	
	
	@Override
	public boolean generatesNewDataSet() {
		return true;
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
	
}
