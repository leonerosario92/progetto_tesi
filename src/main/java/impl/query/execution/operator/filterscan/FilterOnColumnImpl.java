package impl.query.execution.operator.filterscan;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import context.DataType;
import dataset.IDataSet;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.execution.operator.filterscan.FilterOnColumnArgs;
import query.execution.operator.filterscan.FilterOnColumnFunction;
import utils.StatementsEvaluator;

public class FilterOnColumnImpl extends FilterOnColumnFunction {


	@Override
	public BitSet apply(FilterOnColumnArgs args) {
		
		FieldDescriptor column = args.getField();
		Iterator<?> columnIterator  = args.getInputDataSet().getColumn(column).getColumnIterator();
		BitSet validityBitSet = args.getInputDataSet().getValidityBitSet();
		List<FilterStatement> statements = args.getStatements();
		
		TypeComparator comparator = getComparator(column.getType());
		StatementsEvaluator evaluator = new StatementsEvaluator(comparator, statements);
		
		int index = 0;
		boolean isValid;
		while(columnIterator.hasNext()) {
			Object currentValue = columnIterator.next();
			if (! (isValid = validityBitSet.get(index))) {
				index ++;
				continue;
			}
			boolean evaluation = evaluator.evaluate(currentValue);
			validityBitSet.set(index,evaluation);
			index++;
		}
		
		return validityBitSet;
	}

	
	private TypeComparator getComparator(DataType type) {
		switch (type) {
		case INTEGER:
			return new IntComparator();
		case BIG_DECIMAL:
			return new BigDecimalComparator();
		default:
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}

}
