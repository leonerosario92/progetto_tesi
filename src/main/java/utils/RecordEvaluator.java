package utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import dataset.IRecordMapper;
import datatype.DoubleComparator;
import datatype.TypeComparator;
import impl.base.RecordMapper;
import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.CFNode;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;

public class RecordEvaluator {

	private Predicate<Object[]> evaluator;
	private IRecordMapper recordMapper;

	public RecordEvaluator(Map<String, Integer> mapping, Set<CFNode> statements) {
		this(new RecordMapper(mapping),statements);
	}
	
	
	public RecordEvaluator(IRecordMapper recordMapper, Set<CFNode> statements) {
		Iterator<CFNode> it = statements.iterator();
		
		Predicate<Object[]> evaluator = null;
		CFNode statement;
		this.recordMapper = recordMapper;

		if (it.hasNext()) {
			statement = it.next();
			evaluator = getStatementEvaluator(statement);
		}
		while (it.hasNext()) {
			statement = it.next();
			evaluator = evaluator.and(getStatementEvaluator(statement));
		}
		this.evaluator = evaluator;
	}
	

	public boolean evaluate(Object[] recordToEvaluate) {
		return evaluator.test(recordToEvaluate);
	}
	

	private Predicate<Object[]> getStatementEvaluator(CFNode statement) {

		Predicate<Object[]> recordPredicate = null;
		if (statement instanceof FilterStatement) {
			recordPredicate = getSingleStatementEvaluator((FilterStatement) statement);
		} else if (statement instanceof CFilterStatement) {
			recordPredicate = getComposedStatementEvaluator((CFilterStatement) statement);
		}
		return recordPredicate;
	}

	
	private Predicate<Object[]> getSingleStatementEvaluator(FilterStatement statement) {
		Object rightOperand = statement.getRightOperand();
		FilterStatementType type = statement.getFilterType();
		FieldDescriptor field = statement.getField();
		TypeComparator comparator;
		Predicate<Integer> comparisonEvaluator = type.getComparisonEvaluator();
		
		/*====TODO: Make a "IDescriptor" interface implemented by both FieldDescriptor and AggregationDescriptor===*/
		String fieldKey = null;
		if(statement instanceof AggregateFilterStatement) {
			fieldKey = ((AggregateFilterStatement)statement).getAggregationDescriptor().getKey();
			comparator = new DoubleComparator();
		}else if(statement instanceof FilterStatement) {
			fieldKey = statement.getField().getKey();
			comparator = field.getType().getComparator();
		}else {
			comparator = null;
		}
		/*====================================================================*/

		int fieldIndex = recordMapper.getIndex(fieldKey);
		Predicate<Object[]> evaluator = null;
		if(rightOperand instanceof FieldDescriptor) {
			int operandIndex = recordMapper.getIndex(((FieldDescriptor)rightOperand).getKey());
			evaluator = new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] record) {
					int comparisonResult = comparator.compare(record[fieldIndex ], record[operandIndex]);
					return comparisonEvaluator.test(comparisonResult);
				}
			};
		}
		else {
			evaluator = new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] record) {
					int comparisonResult = comparator.compare(record[fieldIndex], rightOperand);
					return comparisonEvaluator.test(comparisonResult);
				}
			};
		}
		return evaluator;
	}

	
	private Predicate<Object[]> getComposedStatementEvaluator(CFilterStatement composedStatement) {

		Iterator<CFNode> it = composedStatement.getstatementSequence().iterator();
		CFNode currentNode = it.next();
		LogicalOperand chainingOperand = currentNode.getChainingOperand();
		Predicate<Object[]> result = getStatementEvaluator(currentNode);

		while (it.hasNext()) {
			currentNode = it.next();
			Predicate<Object[]> currentEvaluator = getStatementEvaluator(currentNode);
			result = chain(result, currentEvaluator, chainingOperand);

			if (currentNode.hasNextStatement()) {
				chainingOperand = currentNode.getChainingOperand();
			}
		}

		return result;
	}

	
	private Predicate<Object[]> chain(Predicate<Object[]> oldEvaluator, Predicate<Object[]> currentEvaluator,
			LogicalOperand chainingOperand) {

		Predicate<Object[]> result = null;

		switch (chainingOperand) {
		case AND:
			result = oldEvaluator.and(currentEvaluator);
			break;
		case OR:
			result = oldEvaluator.or(currentEvaluator);
			break;
		}
		
		return result;
	}

}
