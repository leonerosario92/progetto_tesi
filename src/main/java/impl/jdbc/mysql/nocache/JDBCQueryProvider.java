package impl.jdbc.mysql.nocache;

import java.util.HashMap;
import java.util.concurrent.Callable;

import dataIterator.IDataIterator;
import query.IQueryProvider;
import query.IRelOperator;
import query.RelOperatorType;

public abstract class JDBCQueryProvider implements IQueryProvider{
	
	//Implemented by subclasses depending on specific query language 
	private HashMap<RelOperatorType, Callable<?>> queryImplementations; 

	public void setOperatorImplementation(IRelOperator implementation) {
		queryImplementations.put(key, value)
	}

	
	
}
