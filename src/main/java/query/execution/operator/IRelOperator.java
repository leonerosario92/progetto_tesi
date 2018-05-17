package query.execution.operator;

import query.execution.IExecutableQuery;

public interface IRelOperator {
	
	public RelOperatorType getType();
	
	public IQueryFunction<?> getFunction();
	
	public IExecutableQuery getQuery();
}
