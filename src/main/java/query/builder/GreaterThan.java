package query.builder;


import java.util.function.Predicate;

import model.FieldDescriptor;


public  class GreaterThan/*<T extends Number & Comparable<T>>*/ extends ComparisonFilterStatement/*<T>*/{

	private static PredicateType type = PredicateType.GREATER_THAN;
	
	
	public  GreaterThan( FieldDescriptor field, Object operand) {
		super(type, field, operand);
	}

	/*
	@Override
	public Predicate<T> getPredicate() {
		return new Predicate<T>() {

			@Override
			public boolean test(T leftOperand) {
				return leftOperand.compareTo(getRightOperand()) > 0;
			}
		};
	}
*/
}
