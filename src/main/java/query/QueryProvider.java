package query;

import java.util.HashMap;

import query.execution.ExecutionPlanItem;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.IRelOperator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filterscan.FilterScanFunction;

public class QueryProvider  {
	
	private HashMap<RelOperatorType,Class<?>> implementations;
	
	public QueryProvider() {
		implementations = new HashMap<>();
	}
	
//	public QueryProvider(Iterable<IRelOperator> operators) {
//		this();
//		for(IRelOperator operator : operators){
//			setImplementation(operator);
//		}
//	}
//	
	private void setImplementation(RelOperatorType type, Class<?> clazz) {
		implementations.put(type, clazz);
	}

	
//	public IExecutionPlanItem getQuery(RelOperatorType type) {
//		checkImplementation(type);
//		return implementations.get(type).getQuery();
//	}
	

	private void checkImplementation(RelOperatorType operatorType) {
		if(!(implementations.containsKey(operatorType))) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}
	
	public FilterScanFunction getFilterScanImpl () {
		checkImplementation(RelOperatorType.FILTER_SCAN);
		Class<?> clazz = implementations.get(RelOperatorType.FILTER_SCAN);
		try {
			return (FilterScanFunction) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			//TODO: Manage exception properly
			throw new IllegalArgumentException();
		}
		
	}
	
	public void setFilterScanImpl(Class<FilterScanFunction> functionClass) {
		setImplementation(RelOperatorType.FILTER_SCAN, functionClass);
	}	
	
}
