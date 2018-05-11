package query.operator.projection;

import query.operator.AbstractQueryNode;
import query.operator.IQueryParams;
import query.operator.RelOperatorType;

public class ProjectionQueryNode extends AbstractQueryNode{
private IProjectionQueryParams params;
	
	public ProjectionQueryNode(IProjectionQueryParams params) {
		super(RelOperatorType.PROJECTION);
		this.params = params;
	}

	public IQueryParams getParams() {
		return params;
	}

}
