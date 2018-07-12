package query.execution;

import java.util.Set;

import dataset.IDataSet;
import query.ImplementationProvider;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.RelOperatorType;
import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;


public abstract class Operator<F extends IOperatorFunction, A extends IOperatorArgs> implements ExecutionPlanElement{
	
	protected A args;
	protected F function;
	protected String operatorName;
	protected ExecutionReport report;
	
	
	public Operator(ImplementationProvider provider, RelOperatorType type) { }	
	
	
	public A getArgs() {
		return args;
	}
	
	
	public ExecutionReport getReport() {
		return report;
	}
	
	
	public abstract IDataSet execOperator(IQueryExecutor executor) throws QueryExecutionException;
	
	
	public  abstract void setInputData(IDataSet...inputData);
	
	
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
		addRepresentation(printer);
	}
	
	
	
}
