package query;


public abstract class QueryPlanner implements IQueryPlanner {

	protected QueryProvider queryProvider;
	
	public QueryPlanner() {}

	public void setQueryProvider(QueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}
	
}
