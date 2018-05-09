package query;

import dataIterator.IDataIterator;

public interface IRelOperator {
	
	public RelOperatorType getType();
	
	public IDataIterator exec(Object...args);
	
}
