package query;


public abstract class AbstractQueryPlanner implements IQueryPlanner {

	protected QueryProvider queryProvider;
	
	public AbstractQueryPlanner(QueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}
	
}
