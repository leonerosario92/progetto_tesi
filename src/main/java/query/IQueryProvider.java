package query;

import java.util.concurrent.Callable;

import dataIterator.IDataIterator;

public interface IQueryProvider {
	
	public void setOperatorImplementation(Callable<?> implementation);
	
	public IDataIterator exec (RelationalOperatorType operator, Object...params);
	
}
