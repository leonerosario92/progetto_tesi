package dispatcher;

import datasource.IDataSource;
import query.IQueryPlanner;
import query.execution.IQueryExecutor;

public abstract class QueryDispatcher   implements IQueryDispatcher{

	protected IQueryPlanner planner;
	protected IDataSource dataSource;
	protected IQueryExecutor executor;
	
	public QueryDispatcher() {}

	public void setDataSource(IDataSource dataSource) {
		this.dataSource = dataSource;		
	}

	public void setQueryPlanner(IQueryPlanner queryPlanner) {
		this.planner = queryPlanner;		
	}

	public void setQueryExecutor(IQueryExecutor queryExecutor) {
		this.executor = queryExecutor;
	}
	
	
	
	
}
