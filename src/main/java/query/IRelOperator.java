package query;

import dataIterator.IDataIterator;
import datacontext.IDataContext;

public interface IRelOperator {
	
	public RelOperatorType getType();
	
	public <T extends IQueryParams> IDataIterator exec(IDataContext context,T params);
	
}
