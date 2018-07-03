package impl.query.execution.operator.filteronmultiplecolumn;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dataset.IDataSet;
import dataset.IRecordIterator;
import model.FieldDescriptor;
import query.builder.statement.CFNode;
import query.builder.statement.FilterStatement;
import query.execution.QueryExecutionException;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnArgs;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnFunction;
import utils.RecordEvaluator;

public class FilterOnMultipleColumnImpl extends FilterOnMultipleColumnFunction {

	@Override
	public IDataSet apply(IDataSet inputSet, FilterOnMultipleColumnArgs args) throws QueryExecutionException {
		Set<FieldDescriptor> columns = args.getFields();
		IRecordIterator recordIterator = inputSet.getRecordIterator();
		BitSet validityBitset = inputSet.getValidityBitSet();
		Set<CFNode> statements = args.getStatements();
		
		RecordEvaluator evaluator = 
				new RecordEvaluator(getNameIndexMapper(recordIterator), statements);
				
		int index = 0;
		boolean isValid;
		while(recordIterator.hasNext()) {
			recordIterator.next();
			Object[] currentRecord = recordIterator.getCurrentRecord();
			if (! (isValid = validityBitset.get(index))) {
				index ++;
				continue;
			}
			boolean evaluation = evaluator.evaluate(currentRecord);
			validityBitset.set(index,evaluation);
			index++;
		}
		inputSet.updateValidityBitset(validityBitset);
		return inputSet;
	}
	

	public Map<String,Integer> getNameIndexMapper(IRecordIterator recordIterator){
		Map<String,Integer> mapper = new HashMap<>();
		int fieldsCount = recordIterator.getFieldsCount();
		for(int index = 1; index <= fieldsCount; index++) {
			mapper.put(recordIterator.getColumnName(index),index);
		}
		return mapper;
	}
}


