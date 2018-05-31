package query;

import java.util.HashMap;

import impl.query.execution.operator.filterscan.StreamFilterScan;
import query.execution.ExecutionPlanItem;
import query.execution.operator.IOperatorFunction;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filterscan.FilterScanFunction;

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
	
	
	public void setFilterScanImpl(Class<? extends StreamFilterScan> class1) {
		setImplementation(RelOperatorType.FILTER_SCAN, class1);
	}	
	
	
	private void setImplementation(RelOperatorType type, Class<?> clazz) {
		implementations.put(type, clazz);
	}

	
}
