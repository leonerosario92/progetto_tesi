package query;

import dataIterator.IDataIterator;

public interface IRelationalOperator {
	
	public RelationalOperatorType getType();
	
	public IDataIterator exec();
	
}
