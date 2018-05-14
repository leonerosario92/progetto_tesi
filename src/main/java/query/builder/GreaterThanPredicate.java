package query.builder;

import java.util.function.Predicate;

import model.FieldDescriptor;

public class GreaterThanPredicate<T> implements IFilterPredicate <T> {

	private final PredicateType type = PredicateType.GREATER_THAN;
	FieldDescriptor field;
	T argument;
	
	public GreaterThanPredicate(FieldDescriptor field, T argument) {
		//TODO verificare compatibilità parametri
		this.field = field;
		this.argument = argument;
	}


	@Override
	public FieldDescriptor getField() {
		return field;
	}


	@Override
	public T getArgument() {
		return argument;
	}


	@Override
	public PredicateType getPredicateType() {
		return type;
	}

	

}
