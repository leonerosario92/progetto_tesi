package query;


public abstract class QueryPlanner implements IQueryPlanner {

	protected ImplementationProvider queryProvider;
	
	public QueryPlanner() {}

	public void setQueryProvider(ImplementationProvider queryProvider) {
		this.queryProvider = queryProvider;
	}
	
}
