package query;

import dataset.IDataSet;
import dataset.IEntity;
import query.operator.IQueryParams;

public interface IExecutableQuery {
	
	public void setInputDataSet(IDataSet inputSet);
	
	public void setParams (IQueryParams params);
	
	public IDataSet execQuery();
	
}
