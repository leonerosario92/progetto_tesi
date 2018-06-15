package query;

import java.util.HashMap;

import query.execution.ProcessDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.loadcolumn.LoadColumnFunction;

public class ImplementationProvider  {
	
	private HashMap<RelOperatorType,Class<?>> implementations;
	
	public ImplementationProvider() {
		implementations = new HashMap<>();
	}
	

	private void checkImplementation(RelOperatorType operatorType) {
		if(!(implementations.containsKey(operatorType))) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}
	
	
	
	public void setFilterOnColumnImpl(Class<? extends FilterOnColumnFunction> function) {
		setImplementation(RelOperatorType.FILTER_ON_COLUMN, function);
	}	
	public FilterOnColumnFunction getFilterOnColumnImpl () {
		return(FilterOnColumnFunction) getImplementationInstance(RelOperatorType.FILTER_ON_COLUMN);
	}
	
	public void setLoadColumnImpl(Class<? extends LoadColumnFunction> function) {
		setImplementation(RelOperatorType.LOAD_COLUMN, function);
	}
	public LoadColumnFunction getLoadColumnImpl () {
		return (LoadColumnFunction) getImplementationInstance(RelOperatorType.LOAD_COLUMN);
	}
	
	
	private Object getImplementationInstance(RelOperatorType type) {
		checkImplementation(type);
		Class<?> clazz = implementations.get(type);
		try {
			return  clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}
	
	
	private void setImplementation(RelOperatorType type, Class<?> clazz) {
		implementations.put(type, clazz);
	}

	
}
