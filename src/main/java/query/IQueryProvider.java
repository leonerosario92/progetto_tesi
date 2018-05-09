package query;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import query.filter.IFilterQueryParams;

public interface IQueryProvider {
	
	public void setOperator (IRelOperator operator);
	
	public IDataIterator exec (IDataContext context, IQueryNode node);

}
