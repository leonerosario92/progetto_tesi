package query.operator.filter;

import query.operator.AbstractQueryNode;
import query.operator.IQueryParams;
import query.operator.RelOperatorType;

public class FilterQueryNode extends AbstractQueryNode{
	
	private IFilterQueryParams params;
	
	public FilterQueryNode(IFilterQueryParams params) {
		super(RelOperatorType.PROJECTION);
		this.params = params;
	}

	public IQueryParams getParams() {
		return params;
	}
}
