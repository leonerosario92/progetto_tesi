package query.execution;

import dataset.IDataSet;
import query.execution.operator.IQueryParams;

public interface IExecutableQuery extends IExecutableSet {
	
	public void setInputDataSet(IDataSet inputSet);
	
	public void setParams (IQueryParams params);
	
	public IDataSet execQuery();
	
}
