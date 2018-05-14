package query;

import java.util.HashMap;

import query.function.IQueryFunction;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;

public class QueryProvider  {
	
	private HashMap<RelOperatorType,IRelOperator> implementations;
	
	public QueryProvider() {
		implementations = new HashMap<>();
	}
	
	public QueryProvider(Iterable<IRelOperator> operators) {
		this();
		for(IRelOperator operator : operators){
			setImplementation(operator);
		}
	}
	
	public void setImplementation(IRelOperator operator) {
		implementations.put(operator.getType(), operator);
	}

	
	public IExecutableQuery getQuery(RelOperatorType type) {
		checkImplementation(type);
		return implementations.get(type).getQuery();
	}
	

	private void checkImplementation(RelOperatorType operatorType) {
		if(!(implementations.containsKey(operatorType))) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}
	
	
}
