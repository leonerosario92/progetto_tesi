package query.builder;

import java.util.function.Predicate;

import model.FieldDescriptor;


public interface IFilterStatement /*<T>*/ {
	
	PredicateType getPredicateType();
	
	public FieldDescriptor getField();
	
	public Object getRightOperand();
	
	public String writeSql();
	
	//public Predicate<T> getPredicate();
	
}
