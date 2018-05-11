package query.operator;

import context.IContext;
import dataset.IDataIterator;
import dataset.IEntity;

public interface IRelOperator <I,P> {
	
	public RelOperatorType getType();
	
	public IEntity exec(I input, P params);
	
}
