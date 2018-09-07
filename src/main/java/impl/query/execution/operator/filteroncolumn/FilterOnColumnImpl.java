package impl.query.execution.operator.filteroncolumn;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import dataset.IDataSet;
import datatype.DataType;
import datatype.TypeComparator;
import model.FieldDescriptor;
import query.builder.statement.FilterStatement;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import utils.StatementsEvaluator;

public class FilterOnColumnImpl extends FilterOnColumnFunction {

	@Override
	public IDataSet apply(IDataSet dataSet, FilterOnColumnArgs args) {
		return dataSet;
//		FieldDescriptor column = args.getField();
//		Iterator<?> columnIterator  = dataSet.getColumn(column).getColumnIterator();
//		BitSet validityBitSet = dataSet.getValidityBitSet();
//		Set<FilterStatement> statements = args.getStatements();
//		
//		TypeComparator comparator = getComparator(column.getType());
//		StatementsEvaluator evaluator = new StatementsEvaluator(comparator, statements);
//		
//		int index = 0;
//		boolean isValid;
//		while(columnIterator.hasNext()) {
//			Object currentValue = columnIterator.next();
//			if (! (isValid = validityBitSet.get(index))) {
//				index ++;
//				continue;
//			}
//			boolean evaluation = evaluator.evaluate(currentValue);
//			validityBitSet.set(index,evaluation);
//			index++;
//		}
//		dataSet.updateValidityBitset(validityBitSet);
//		return dataSet;
	}

	
	private TypeComparator getComparator(DataType type) {
		return type.getComparator();
	}



}