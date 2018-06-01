package impl.base;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.QueryDispatcher;
import impl.query.execution.ExecutionException;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.IQueryExecutor;

public class BaseQueryDispatcher extends QueryDispatcher {
	
	public BaseQueryDispatcher () {
		super ();
	}
	
	public BaseQueryDispatcher getInstance() {
		return new BaseQueryDispatcher();
	}

	@Override
	public IRecordIterator dispatchQuery(Query query) {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		IDataSet result;
		query.setExecutionStartTime();
		try {
			result = executor.executePlan(queryPlan);
			return result.tableIterator();
		} catch (ExecutionException e) {
			
		}
		finally {
			query.setExecutionEndTime();
		}
		return null;	
	}
}

