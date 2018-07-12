package query.execution;

import dataset.IDataSet;
import query.ImplementationProvider;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.RelOperatorType;

public abstract class ProcessDataSetOperator<F extends DataSetProcessingFunction, A extends IOperatorArgs> extends Operator<F,A>{
	
	private boolean hasInputDataSet;
	private IDataSet inputDataSet;
	
	public ProcessDataSetOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type);
		hasInputDataSet = false;
	}
	
	
	public  void setInputData(IDataSet...inputData) {
		if(hasInputDataSet || inputData.length != 1) {
			throw new IllegalStateException("ProcessDataSet Operators accepts only one input DataSet.");
		}
		this.inputDataSet = inputData[0];
		hasInputDataSet = true;
	}

	
	public  IDataSet execOperator(IQueryExecutor executor) throws QueryExecutionException {
		
		if(! hasInputDataSet) {
			throw new IllegalStateException("ProcessDataSet Operators cannot be executed if an input DataSet has not been set."); 
		}
		
		return function.apply(inputDataSet, args);
	}
	
}
