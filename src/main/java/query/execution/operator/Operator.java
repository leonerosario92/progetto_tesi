package query.execution.operator;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.ImplementationProvider;
import query.execution.IReportableExecutable;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorReport;


public abstract class Operator<F extends IOperatorFunction, A extends IOperatorArgs> {
	
	protected A args;
	protected F function;
	protected String operatorName;
	

	public A getArgs() {
		return args;
	}
	
	public void setArgs (A args) {
		this.args = args;
	}
}
