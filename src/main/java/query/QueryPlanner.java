package query;


public abstract class QueryPlanner implements IQueryPlanner {

	protected ImplementationProvider implementationProvider;
	
	public QueryPlanner() {}

	public void setQueryProvider(ImplementationProvider queryProvider) {
		this.implementationProvider = queryProvider;
	}
	
}
