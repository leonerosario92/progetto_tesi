package query.execution;

import dataset.IDataSet;
import dataset.IEntity;

public interface IQueryExecutor {
	 
	public IDataSet executeQuery(IExecutableQuery query, IDataSet inputSet);
	
}
