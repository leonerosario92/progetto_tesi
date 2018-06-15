package query.execution;

import java.util.concurrent.Callable;

import dataset.IDataSet;
import query.ImplementationProvider;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.RelOperatorType;
import utils.IResultHolder;
import utils.TreePrinter;
import utils.report.ExecutionReport;


public abstract class Operator<F extends IOperatorFunction, A extends IOperatorArgs> implements ExecutionPlanElement{
	
	protected A args;
	protected F function;
	protected String operatorName;
	protected ExecutionReport report;

	
	
	public Operator(ImplementationProvider provider, RelOperatorType type) { 
		this.report = new ExecutionReport();
	}
	
	
//	public abstract IResultHolder<IDataSet> execute (IQueryExecutor executor);
	
	
	public A getArgs() {
		return args;
	}
	
	
	public ExecutionReport getReport() {
		return report;
	}
	
	
	@Override
	public void addRepresentation(TreePrinter printer) {
		printer.appendLine("[OPERATOR] ");
		printer.addIndentation();
		printer.appendLine("TYPE : " + operatorName);
		printer.appendLine("ARGS : { " + args.getStringRepresentation() + " }");
		printer.removeIndentation();
		printer.appendLine("[END OPERATOR]");

	}
	
	
	
	public void addRepresentationWithReport(TreePrinter printer ) {
		printer.appendLine("[OPERATOR] ");
		printer.addIndentation();
		printer.appendLine("TYPE : " + operatorName);
		printer.appendLine("ARGS : { " + args.getStringRepresentation() + " }");
		printer.appendLine("EXECUTION REPORT : " + report.getStringRepresentation());
		printer.removeIndentation();
		printer.appendLine("[END OPERATOR]");
	}
	
	
	
}
