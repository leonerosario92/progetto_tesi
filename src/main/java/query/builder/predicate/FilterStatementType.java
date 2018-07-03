package query.builder.predicate;

import java.util.function.Predicate;

import model.FieldDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.FilterStatement;

public enum FilterStatementType {
	GREATER_THAN (QueryConstants.GREATER_THAN , GreaterThan.class), 
	GREATER_THAN_OR_EQUAL(QueryConstants.GREATER_THAN_OR_EQUALS, GreaterThanOrEqual.class),
	LESS_THAN (QueryConstants.LESS_THAN, LessThan.class), 
	LESS_THAN_OR_EQUAL(QueryConstants.LESS_THAN_OR_EQUALS, LessThanOrEquals.class),
	EQUALS_TO(QueryConstants.EQUALS_TO, EqualsTo.class),
	DIFFERENT_FROM(QueryConstants.DIFFERENT_FROM, DifferentFrom.class);
	
	
	public String representation;
	private Predicate<Integer> comparisonEvaluator;
	private Class<? extends FilterStatement> statementClass;
	
	
	FilterStatementType (String representation, Class<? extends FilterStatement> statementClass) {
		this.representation = representation;
		this.statementClass = statementClass;
	}
	
	
	public Predicate<Integer> getComparisonEvaluator(){
		return getComparisonEvaluator(this);
	}
	
	
	public FilterStatement getInstance(FieldDescriptor field, Object operand) {
		FilterStatement statement;
		try {
			statement = statementClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
		statement.setField(field);
		statement.setOperand(operand);
		return statement;
	}
	
	
	private Predicate<Integer> getComparisonEvaluator(FilterStatementType type) {
		switch (type) {
		case EQUALS_TO:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value == 0;
				}
			};
		case DIFFERENT_FROM:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value != 0;
				}
			};
		case GREATER_THAN:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value > 0;
				}
			};
		case GREATER_THAN_OR_EQUAL:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value >= 0;
				}
			};
		case LESS_THAN:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value < 0;
				}
			};
		case LESS_THAN_OR_EQUAL:
			return new Predicate<Integer>() {
				@Override
				public boolean test(Integer value) {
					return value <= 0;
				}
			};

		default:
			// TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}

}
 