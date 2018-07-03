package query.builder;

import context.Context;

public class OrderByBuilder {

	private Context context;
	private Query query;
	
	public OrderByBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	
	public Query getQuery() {
		return query;
	}
	
}
