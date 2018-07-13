package query.execution;

import java.util.ArrayList;

import dataset.IDataSet;
import dataset.ILayoutManager;
import query.ImplementationProvider;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.MaterializationFunction;
import query.execution.operator.RelOperatorType;
import query.execution.operator.SortDataSetFunction;

public abstract class SortDataSetOperator<F extends SortDataSetFunction, A extends IOperatorArgs> extends Operator<F,A> {

	private IDataSet inputDataSet;
	private boolean hasInputDataSet;
	
	
	public SortDataSetOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type, true);
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
		
		ILayoutManager layoutManager = executor.getlayoutManager();
		
		return function.apply(inputDataSet, layoutManager,  args);
	}
	
}
