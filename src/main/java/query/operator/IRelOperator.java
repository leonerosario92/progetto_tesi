package query.operator;

import query.IExecutableQuery;

public interface IRelOperator {
	
	public RelOperatorType getType();
	
	public IQueryFunction<?> getFunction();
	
	public IExecutableQuery getQuery();
}
