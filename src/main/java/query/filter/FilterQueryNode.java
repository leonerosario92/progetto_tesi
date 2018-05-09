package query.filter;

import query.AbstractQueryNode;
import query.IQueryParams;
import query.RelOperatorType;

public class FilterQueryNode extends AbstractQueryNode{
	
	private IFilterQueryParams params;
	
	public FilterQueryNode(IFilterQueryParams params) {
		super(RelOperatorType.FILTER);
		this.params = params;
	}

	public IQueryParams getParams() {
		return params;
	}
}
