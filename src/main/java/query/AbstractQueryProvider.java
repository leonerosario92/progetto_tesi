package query;

import java.util.HashMap;
import java.util.function.Predicate;

import context.DataContext;
import context.IContext;
import dataset.IDataIterator;
import model.IFieldDescriptor;
import query.operator.IQueryNode;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;
import query.operator.filter.FilterOperator;

public abstract class AbstractQueryProvider implements IQueryProvider {

	private HashMap<RelOperatorType, IRelOperator> implementations;
	private IContext context;
	
	public AbstractQueryProvider(IContext context) {
		this.context = context;
	}
	
	public void setOperator (IRelOperator operator) {
		implementations.put(operator.getType(), operator);
	}
	
	public IDataIterator execQuery (IContext context, IQueryNode node) {
		RelOperatorType type = node.getOperatorType();
		checkImplementation(type);
		IDataIterator result =
				implementations.get(type).exec(context, node.getParams());
		return result;
	}

	public void checkImplementation(RelOperatorType type) {
		if(! implementations.containsKey(type)) {
			//TODO Manage exception properly
			throw new UnsupportedOperationException();
		}
	}
}
