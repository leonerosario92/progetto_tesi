package query;

import context.IContext;
import dataset.IDataIterator;
import query.operator.IQueryNode;
import query.operator.IRelOperator;
import query.operator.filter.IFilterQueryParams;


public interface IQueryProvider {
	
	public void setOperator (IRelOperator operator);
	
	public IDataIterator execQuery (IContext context, IQueryNode node);
	
	public IDataIterator execBlock (IContext context, IExecutionBlock block);

}
