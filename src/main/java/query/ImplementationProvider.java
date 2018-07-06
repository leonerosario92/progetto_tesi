package query;

import java.util.HashMap;

import query.execution.operator.RelOperatorType;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnFunction;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionFunction;
import query.execution.operator.orderby.OrderByFunction;

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
	
	
	public void setFilterOnMultipleColumnImpl(Class<? extends FilterOnMultipleColumnFunction> function) {
		setImplementation(RelOperatorType.FILTER_ON_MULTIPLE_COLUMN, function);
	}	
	public FilterOnMultipleColumnFunction getFilterOnMultipleColumnImpl () {
		return(FilterOnMultipleColumnFunction) getImplementationInstance(RelOperatorType.FILTER_ON_MULTIPLE_COLUMN);
	}
	
	
	public void setLoadColumnImpl(Class<? extends LoadColumnFunction> function) {
		setImplementation(RelOperatorType.LOAD_COLUMN, function);
	}
	public LoadColumnFunction getLoadColumnImpl () {
		return (LoadColumnFunction) getImplementationInstance(RelOperatorType.LOAD_COLUMN);
	}
	
	
	public void setLoadVerticalPartitionImpl(Class<? extends LoadVerticalPartitionFunction > function) {
		setImplementation(RelOperatorType.LOAD_VERTICAL_PARTITION, function);
	}
	
	public LoadVerticalPartitionFunction getLoadColumnSubsetImpl() {
		return (LoadVerticalPartitionFunction) getImplementationInstance(RelOperatorType.LOAD_VERTICAL_PARTITION);
	}
	
	
	public void setOrderByImpl (Class<? extends OrderByFunction> function) {
		setImplementation(RelOperatorType.ORDER_BY, function);
	}

	public OrderByFunction getOrderByImpl() {
		return (OrderByFunction) getImplementationInstance(RelOperatorType.ORDER_BY);
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
