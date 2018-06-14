package query.execution;

import dataset.IDataSet;
import query.QueryProvider;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.RelOperatorType;

public abstract class ProcessDataSetOperator<F extends DataSetProcessingFunction, A extends IOperatorArgs> extends Operator<F,A>{
	
	
	public ProcessDataSetOperator(QueryProvider provider, RelOperatorType type) {
		super(provider, type);
	}

	
	public IDataSet processDataSet(IDataSet inputSet) throws Exception {
		IDataSet result =  (IDataSet) function.apply(inputSet,args);
		return result;
	}
	



	
}
