package query;

import java.util.HashMap;

import context.IContext;
import dataset.IDataIterator;
import query.operator.IQueryNode;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;
import query.operator.filter.IFilterQueryParams;

public class QueryProvider implements IQueryProvider {
	
	private IContext context;
	private HashMap<RelOperatorType,IRelOperator> implementations;
	
	public QueryProvider(IContext context) {
		this.context = context;
		implementations = new HashMap<RelOperatorType, IRelOperator>();
	}
	
	public QueryProvider(IContext context, Iterable<IRelOperator> implementations) {
		this(context);
		for(IRelOperator operator : implementations){
			setOperator(operator);
		}
	}
	
	public void setOperator(IRelOperator operator) {
		implementations.put(operator.getType(),operator);
	}

	public IDataIterator execQuery(IContext context, IQueryNode node) {
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
