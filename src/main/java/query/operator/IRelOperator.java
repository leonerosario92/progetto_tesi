package query.operator;

import query.IExecutableQuery;
import query.function.IQueryFunction;

public interface IRelOperator {
	
	public RelOperatorType getType();
	
	public IQueryFunction<?> getFunction();
	
	public IExecutableQuery getQuery();
}
