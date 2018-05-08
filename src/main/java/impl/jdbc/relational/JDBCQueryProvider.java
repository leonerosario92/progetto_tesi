package impl.jdbc.relational;

import java.util.HashMap;
import java.util.concurrent.Callable;

import dataIterator.IDataIterator;
import query.IQueryProvider;
import query.RelationalOperatorType;

public abstract class JDBCQueryProvider implements IQueryProvider{
	
	//Implemented by subclasses depending on specific query language 
	private HashMap<RelationalOperatorType, Callable<?>> queryImplementations; 

	public void setOperatorImplementation(RelationalOperator implementation) {
		queryImplementations.put(key, value)
	}

	public IDataIterator exec(RelationalOperatorType operator, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
