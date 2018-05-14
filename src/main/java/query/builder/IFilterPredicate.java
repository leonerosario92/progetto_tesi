package query.builder;

import model.FieldDescriptor;


public interface IFilterPredicate <T> {
	
	PredicateType getPredicateType();
	
	public FieldDescriptor getField();
	
	public T getArgument();
	
}
