package query.operator.filter;

import dataset.IEntity;
import query.operator.AbstractRelOperator;
import query.operator.IQueryParams;
import query.operator.RelOperatorType;

public  class FilterOperator extends AbstractRelOperator {
	
	IFilterFunction function;
	
	public FilterOperator(IFilterFunction function) {
		super(RelOperatorType.FILTER);
		this.function = function;
	}

	@Override
	public IEntity exec(IEntity input, IQueryParams params) {
		if(!(params instanceof IFilterQueryParams)) {
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
		return function.apply(input, (IFilterQueryParams) params);
	}

	
	


	

	


	

	

	

	
	
}