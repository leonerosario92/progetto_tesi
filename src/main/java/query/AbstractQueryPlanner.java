package query;

import context.Context;

public abstract class AbstractQueryPlanner implements IQueryPlanner {

	protected QueryProvider queryProvider;
	
	public AbstractQueryPlanner(Context context) {
		this.queryProvider = context.getQueryProvider();
	}
	
}
