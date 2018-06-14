package query.execution.operator;

import dataset.IDataSet;
import query.execution.QueryExecutionException;

public interface DataSetProcessingFunction <A> extends IOperatorFunction {
	
	public IDataSet apply (IDataSet inputSet, A args) throws QueryExecutionException ;
	
}
