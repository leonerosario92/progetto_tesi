package query.filter;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import query.AbstractRelOperator;
import query.IQueryParams;
import query.RelOperatorType;

public  class FilterOperator extends AbstractRelOperator {
	
	IFilterFunction function;
	
	public FilterOperator(IFilterFunction function) {
		super(RelOperatorType.FILTER);
		this.function = function;
	}

	public <T extends IQueryParams> IDataIterator exec(IDataContext context, T p) {
		
		if(! (p instanceof IFilterQueryParams)) {
			//TODO manage exception properly
			throw new IllegalArgumentException();
		}
		
		IFilterQueryParams params = (IFilterQueryParams) p;
	
		IDataIterator result = function.apply(context, params );
		
		return result;
	}


	

	

	

	
	
}