package query;

import java.util.HashMap;
import java.util.function.Predicate;

import dataIterator.IDataIterator;
import datacontext.DataContext;
import datacontext.IDataContext;
import model.IField;
import query.filter.FilterOperator;

public abstract class AbstractQueryProvider implements IQueryProvider {

//	private HashMap<RelOperatorType, IRelOperator> implementations;
//	private IDataContext context;
//	
//	public AbstractQueryProvider(IDataContext context) {
//		this.context = context;
//	}
//	
//	public void setOperator (IRelOperator operator) {
//		implementations.put(operator.getType(), operator);
//	}
//	
//	public IDataIterator filter (DataContext context, IField field, Predicate condition ) {
//		
//		RelOperatorType filterType = RelOperatorType.FILTER;
//		checkImplementation (filterType);
//		FilterOperator filterOperator = (FilterOperator) implementations.get(filterType);
//		IDataIterator result = filterOperator.exec(context,field,condition);
//		return result;
//	}
//	
//	public void checkImplementation(RelOperatorType type) {
//		if(! implementations.containsKey(type)) {
//			//TODO Manage exception properly
//			throw new UnsupportedOperationException();
//		}
//	}
}
