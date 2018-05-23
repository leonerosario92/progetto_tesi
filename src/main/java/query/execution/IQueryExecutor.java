package query.execution;

import dataset.IDataSet;
import dataset.IEntity;
import query.QueryProvider;

public interface IQueryExecutor {
	 
	public IDataSet executePlan(ExecutionPlanBlock plan);

}

