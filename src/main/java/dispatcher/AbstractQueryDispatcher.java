package dispatcher;

import datasource.IDataSource;
import query.IQueryPlanner;
import query.execution.IQueryExecutor;

public abstract class AbstractQueryDispatcher   implements IQueryDispatcher{

	protected IQueryPlanner planner;
	protected IDataSource dataSource;
	protected IQueryExecutor executor;
	
	public AbstractQueryDispatcher(IDataSource dataSource,IQueryPlanner planner, IQueryExecutor executor) {
		this.planner = planner;
		this.executor = executor;
		this.dataSource = dataSource;
	}
	
	
}
