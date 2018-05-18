package query.execution;

import dataset.IDataSet;
import dataset.IEntity;
import query.QueryProvider;

public interface IQueryExecutor {
	 
	public IDataSet executeQuery(IExecutableQuery query, IDataSet inputSet);

	public void setQueryProvider(QueryProvider queryProvider);
	
}

