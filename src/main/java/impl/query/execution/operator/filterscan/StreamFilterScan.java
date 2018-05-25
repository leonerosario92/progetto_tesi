package impl.query.execution.operator.filterscan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class StreamFilterScan extends FilterScanFunction {

	@Override
	public IDataSet apply(FilterScanArgs args) {
		FieldDescriptor column = args.getField();
		Stream<?> inputStream  = args.getInputDataSet().getColumnStream(column);
		List<FilterStatement> statements = args.getStatements();
		for(FilterStatement statement : statements) {
			
			FilterStatementType type = statement.getFilterType();
			Object operand = statement.getRightOperand();
			
			filterOut(inputStream, type, operand);
		}
	}

	
	private void filterOut(Stream<?> inputStream, FilterStatementType type, Object operand ) {
		
		switch(type) {
		case GREATER_THAN:
			applyGreaterThanStream(inputStream,operand);
			break;
		case LESS_THAN:
			applyLessThanStream(inputStream,operand);
			break;
		}
		
	}

	
	private void applyGreaterThanStream(Stream<Integer> inputStream, Object op) {
		inputStream.map((element) -> (element.compareTo(operand) < 0) ? element : null);
	}
	
	
	private void applyLessThanStream(Stream<Integer> inputStream, Object operand) {
		inputStream.map((element) -> (element.compareTo(operand) < 0) ? element : null);
	}

}
