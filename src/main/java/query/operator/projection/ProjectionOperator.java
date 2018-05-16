package query.operator.projection;

import query.IExecutableQuery;
import query.operator.IQueryFunction;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;
import query.operator.filter.FilterFunction;

public class ProjectionOperator implements IRelOperator {

	private static final  RelOperatorType type = RelOperatorType.PROJECTION;
	private ProjectionFunction function;
	
	public ProjectionOperator(ProjectionFunction function) {
		this.function = function;
	}
	
	@Override
	public RelOperatorType getType() {
		return type;
	}

	@Override
	public IQueryFunction<?> getFunction() {
		return function;
	}

	@Override
	public IExecutableQuery getQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
