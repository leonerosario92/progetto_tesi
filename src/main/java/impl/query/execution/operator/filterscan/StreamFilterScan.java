package impl.query.execution.operator.filterscan;

import java.util.ArrayList;
import java.util.Comparator;
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
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class StreamFilterScan extends FilterScanFunction {


	@Override
	public IDataSet apply(FilterScanArgs args) {
		FieldDescriptor column = args.getField();
		Stream<?> inputStream  = args.getInputDataSet().getColumnStream(column);
		List<FilterStatement> statements = args.getStatements();
		
		TypeComparator comparator = getComparator(column.getType());
		
		for(FilterStatement statement : statements) {
			
			FilterStatementType type = statement.getFilterType();
			Object operand = statement.getRightOperand();
			
			Predicate<Integer> predicate = getPredicate(type);
			
			inputStream.map (element -> {
				int comparison = comparator.compare(element, operand);
				boolean result = predicate.test(comparison);
				if(result) {
					return element;
				}else {
					return null;
				}
			});
			
		}
		
	
				
	}

	
	private TypeComparator getComparator(DataType type) {
		switch (type) {
		case INTEGER:
			return new IntComparator();
		default:
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}

	
	private Predicate<Integer> getPredicate(FilterStatementType type) {
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

	
//	private void filterOut(
//			Stream<?> inputStream, 
//			FilterStatementType type, 
//			Object operand,
//			TypeComparator comparator) {
//		
//		switch(type) {
//		case GREATER_THAN:
//			applyGreaterThanStream(inputStream,operand,comparator);
//			break;
//		case LESS_THAN:
//			applyLessThanStream(inputStream,operand);
//			break;
//		}
//		
//	}
//	
//
//	
//	private void applyGreaterThanStream(Stream<?> inputStream, Object operand, TypeComparator comparator) {
//		inputStream.map(
//				(element)->(comparator.compare(element, operand) > 0) ? element : null
//		);
//	}
	
	
	

}
