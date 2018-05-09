package query;

import java.util.HashMap;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import query.filter.IFilterQueryParams;

public class QueryProvider implements IQueryProvider {
	
	private IDataContext context;
	private HashMap<RelOperatorType,IRelOperator> implementations;
	
	public QueryProvider(IDataContext context) {
		this.context = context;
		implementations = new HashMap<RelOperatorType, IRelOperator>();
	}
	
	public QueryProvider(IDataContext context, Iterable<IRelOperator> implementations) {
		this(context);
		for(IRelOperator operator : implementations){
			setOperator(operator);
		}
	}
	
	public void setOperator(IRelOperator operator) {
		implementations.put(operator.getType(),operator);
	}

	public IDataIterator exec(IDataContext context, IQueryNode node) {
		RelOperatorType operatorType = node.getOperatorType();
		checkImplementation(operatorType);
		return implementations.get(operatorType)
				.exec(context, node.getParams());
	}

	private void checkImplementation(RelOperatorType operatorType) {
		if(!(implementations.containsKey(operatorType))) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}

	

}
