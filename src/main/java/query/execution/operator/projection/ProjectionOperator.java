package query.execution.operator.projection;

import query.execution.IExecutableQuery;
import query.execution.operator.IQueryFunction;
import query.execution.operator.IRelOperator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filter.FilterFunction;

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