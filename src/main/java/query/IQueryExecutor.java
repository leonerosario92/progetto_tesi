package query;

import dataset.IEntity;

public interface IQueryExecutor {
	 
	//public IEntity executePlan(IQueryPlan queryPlan);
	
	public IEntity executeQuery(IExecutableQuery query);
	
}
