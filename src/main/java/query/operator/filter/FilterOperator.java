package query.operator.filter;

import dataset.IDataSet;
import query.IExecutableQuery;
import query.function.IQueryFunction;
import query.operator.IQueryParams;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;

public class FilterOperator implements IRelOperator {
	
	private static final  RelOperatorType type = RelOperatorType.FILTER;
	private FilterFunction function;
	
	
	private class FilterExecutableQuery implements IExecutableQuery {
		
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
	
	
	public FilterOperator(FilterFunction function) {
		this.function = function;
	}

	@Override
	public RelOperatorType getType() {
		return type;
	}

	@Override
	public IQueryFunction<?> getFunction() {
		return function;
	}

	@Override
	public IExecutableQuery getQuery() {
		return new FilterExecutableQuery(function);
	}
}