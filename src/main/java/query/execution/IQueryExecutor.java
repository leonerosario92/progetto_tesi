package query.execution;

import dataset.IDataSet;
import dataset.IEntity;
import impl.query.execution.ExecutionException;
import query.QueryProvider;

public interface IQueryExecutor {
	 
	public IDataSet executePlan(ExecutionPlan plan) throws ExecutionException;

}

