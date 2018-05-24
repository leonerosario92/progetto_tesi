package impl.base;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.AbstractQueryDispatcher;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.ExecutionPlanBlock;
import query.execution.IQueryExecutor;

public class BaseQueryDispatcher extends AbstractQueryDispatcher {
	
	private BaseQueryDispatcher (IDataSource datasource, IQueryPlanner planner, IQueryExecutor executor) {
		super (datasource,planner,executor);
	}
	
	public BaseQueryDispatcher getInstance(IDataSource datasource, IQueryPlanner planner, IQueryExecutor executor) {
		return new BaseQueryDispatcher(datasource, planner, executor);
	}

	@Override
	public IRecordIterator dispatchQuery(Query query) {
		ExecutionPlanBlock queryPlan = planner.getExecutionPlan(query);
		IDataSet result = executor.executePlan(queryPlan);
		return result.tableIterator();
	}
}

