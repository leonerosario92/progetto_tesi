package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import datatype.TypeComparator;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;

public class StatementsEvaluator {
	
	TypeComparator comparator;
	Predicate<Object> evaluator;

	
	public StatementsEvaluator(TypeComparator comparator, Set<FilterStatement> statements ) {
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
				type.getComparisonEvaluator();
		
		Predicate<Object> evaluator = new Predicate<Object>() {
			@Override
			public boolean test(Object leftOperand) {
				int result = comparator.compare(leftOperand, rightOperand);
				return resultEvaluator.test(result);
			}
		};
		return evaluator;
	}
	
}
