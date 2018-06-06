package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import impl.query.execution.operator.filterscan.TypeComparator;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;

public class StatementsEvaluator {
	
	TypeComparator comparator;
	Predicate<Object> evaluator;

	
	public StatementsEvaluator(TypeComparator comparator, List<FilterStatement> statements ) {
		this.comparator = comparator;
		
		Iterator<FilterStatement> it = statements.iterator();
		Predicate<Object> evaluator = null;
		FilterStatement statement;
		
		if(it.hasNext()) {
			statement = it.next();
			evaluator = getStatementEvaluator(statement);
		}
		while(it.hasNext()) {
			statement = it.next();
			evaluator = evaluator.and(getStatementEvaluator(statement));
		}
		
		this.evaluator = evaluator;
	}
	
	
	public boolean evaluate (Object leftOperand) {
		return evaluator.test(leftOperand);
	}
	
	
	public Predicate<Object> getStatementEvaluator(FilterStatement statement) {
		Object rightOperand = statement.getRightOperand();
		FilterStatementType type = statement.getFilterType();
		Predicate<Integer >resultEvaluator = 
				getComparisonEvaluator(type);
		
		Predicate<Object> evaluator = new Predicate<Object>() {
			@Override
			public boolean test(Object leftOperand) {
				int result = comparator.compare(leftOperand, rightOperand);
				return resultEvaluator.test(result);
			}
		};
		return evaluator;
	}
	
	
	private Predicate<Integer> getComparisonEvaluator(FilterStatementType type) {
		switch(type) {
		case EQUALS_TO:
			return new Predicate<Integer>(){
				@Override
				public boolean test(Integer value) {
					return value == 0;
				}
			};
		case DIFFERENT_FROM:
			return new Predicate<Integer>(){
				@Override
				public boolean test(Integer value) {
					return value != 0;
				}
			};
		default:
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}
}
	

