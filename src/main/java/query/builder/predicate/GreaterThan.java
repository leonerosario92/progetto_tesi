package query.builder.predicate;


import java.util.function.Predicate;

import query.builder.statement.FilterStatement;


public  class GreaterThan/*<T extends Number & Comparable<T>>*/ extends FilterStatement/*<T>*/{

	private static FilterStatementType type = FilterStatementType.GREATER_THAN;
	
	public   GreaterThan( ) {
		super(type);
	}

	
//	@Override
//	public <T> Predicate<T> getPredicate() {
//		
//		return new Predicate<T>() {
//
//			@Override
//			public boolean test(T leftOperand) {
//				return leftOperand.compareTo(getRightOperand()) > 0;
//			}
//		};
//	}
	

}