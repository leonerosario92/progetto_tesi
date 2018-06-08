package query;

import java.util.HashMap;

import query.execution.ExecutionPlanItem;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filterscan.FilterOnColumnFunction;

public class QueryProvider  {
	
	private HashMap<RelOperatorType,Class<?>> implementations;
	
	public QueryProvider() {
		implementations = new HashMap<>();
	}
	

	private void checkImplementation(RelOperatorType operatorType) {
		if(!(implementations.containsKey(operatorType))) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}
	
	
	public FilterOnColumnFunction getFilterOnColumnImpl () {
		checkImplementation(RelOperatorType.FILTER_ON_COLUMN);
		Class<?> clazz = implementations.get(RelOperatorType.FILTER_ON_COLUMN);
		try {
			return (FilterOnColumnFunction) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
		
	}
	
	
	public void setFilterOnColumnImpl(Class<? extends FilterOnColumnFunction> function) {
		setImplementation(RelOperatorType.FILTER_ON_COLUMN, function);
	}	
	
	
	private void setImplementation(RelOperatorType type, Class<?> clazz) {
		implementations.put(type, clazz);
	}

	
}
