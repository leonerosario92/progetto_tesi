//package impl.query.execution.operator.filterscan;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Set;
//import java.util.function.BiConsumer;
//import java.util.function.BinaryOperator;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//import java.util.stream.Collector;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import context.DataType;
//import dataset.IDataSet;
//import model.FieldDescriptor;
//import query.builder.predicate.FilterStatementType;
//import query.builder.statement.FilterStatement;
//import query.execution.operator.filterscan.FilterOnColumnArgs;
//import query.execution.operator.filterscan.FilterOnColumnFunction;
//
//public class StreamFilterOnColumn extends FilterOnColumnFunction {
//
//
//	@Override
//	public BitSet apply(FilterOnColumnArgs args) {
//		
//		FieldDescriptor column = args.getField();
//		Stream<?> columnStream  = args.getInputDataSet().getColumnStream(column);
////		BitSet bitset = args.getValidityBitSet();
//		List<FilterStatement> statements = args.getStatements();
//		
//		TypeComparator comparator = getComparator(column.getType());
//
//		for(FilterStatement statement : statements) {
//			
//			FilterStatementType type = statement.getFilterType();
//			Object operand = statement.getRightOperand();
//			Predicate<Integer> predicate = getPredicate(type);
//			
//			columnStream.map (element -> {
//				int comparison = comparator.compare(element, operand);
//				boolean result = predicate.test(comparison);
//				if(result) {
//					return true;
//				}else {
//					return false;
//				}
//			});
//			
//		}
//		
//	
//	}
//
//	
//	private TypeComparator getComparator(DataType type) {
//		switch (type) {
//		case INTEGER:
//			return new IntComparator();
//		default:
//			//TODO Manage exception properly
//			throw new IllegalArgumentException();
//		}
//	}
//
//	
//	private Predicate<Integer> getPredicate(FilterStatementType type) {
//		switch(type) {
//		case EQUALS_TO:
//			return new Predicate<Integer>(){
//				@Override
//				public boolean test(Integer value) {
//					return value == 0;
//				}
//			};
//		case DIFFERENT_FROM:
//			return new Predicate<Integer>(){
//				@Override
//				public boolean test(Integer value) {
//					return value != 0;
//				}
//			};
//		default:
//			//TODO Manage exception properly
//			throw new IllegalArgumentException();
//		}
//	}
//	
//}
