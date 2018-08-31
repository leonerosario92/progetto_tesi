package query.execution.operator;

import dataset.IDataSet;
import query.execution.QueryExecutionException;

public interface DataSetProcessingFunction <A> extends ISequentialOperatorFunction {
	
	public IDataSet apply (IDataSet inputSet, A args) throws QueryExecutionException ;
	
}
