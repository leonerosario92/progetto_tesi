package query.operator.filter;

import dataset.IDataSet;
import query.IExecutableQuery;
import query.operator.IQueryParams;

public class FilterExecutableQuery implements IExecutableQuery {
	
	FilterFunction function;
	IDataSet inputSet;
	IFilterQueryParams params;
	
	public FilterExecutableQuery(FilterFunction function) {
		this.function = function;
	}

	@Override
	public void setInputDataSet(IDataSet inputSet) {
		this.inputSet = inputSet;
	}

	@Override
	public void setParams(IQueryParams params) {
		if(!(params instanceof IFilterQueryParams)) {
			//TODO manage exception properly
			throw new IllegalArgumentException();
		}
		
		this.params = (IFilterQueryParams) params;
	}

	@Override
	public IDataSet execQuery() {
		return function.applyFunction(inputSet, params);
	}

	
	
}
