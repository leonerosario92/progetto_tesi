package query.builder.predicate;
//package query.builder;
//
//import java.util.ArrayList;
//import java.util.function.Predicate;
//
//import model.FieldDescriptor;
//
//public final class ComposedPredicate<T> implements IComposedFilterPredicate<T> {
//	
//	private class CompositionNode{
//		public IPredicate<T> predicate;
//		public ChainingOperator operator;
//		public CompositionNode(IPredicate<T> predicate,ChainingOperator operator ) {
//			this.predicate = predicate;
//			this.operator = operator;
//		}
//	}
//	
//	private class CPredicate implements Predicate<T>{
//		
//		private ArrayList<CompositionNode> nodeList;
//		
//		public CPredicate(ArrayList<CompositionNode> nodeList) {
//			this.nodeList = nodeList;
//		}
//
//		@Override
//		public boolean test(T leftOperand) {
//			boolean result = true;
//			for(CompositionNode node : nodeList) {
//				Predicate<T> nodePredicate = node.predicate.getPredicate();
//				if (!(nodePredicate.test(leftOperand))) {
//					result = false;
//					break;
//				}
//			}
//			return result;
//		}
//		
//	}
//
//	private final PredicateType TYPE = PredicateType.COMPOSED;
//	private final String SQL_OPEN_DELIMITER = "(";
//	private final String SQL_CLOSE_DELIMITER = ")";
//	private final String SQL_SPACE_DELIMITER = " ";
//	
//	ArrayList<CompositionNode> statementsChain;
//	FieldDescriptor field;
//	T rightOperand;
//	boolean terminated;
//	
//	
//	public ComposedPredicate() {
//		this.statementsChain = new ArrayList<>();
//		terminated = false;
//	}
//	
//	
//	@Override
//	public PredicateType getPredicateType() {
//		return TYPE;
//	}
//
//	
//	@Override
//	public FieldDescriptor getField() {
//		return field;
//	}
//
//	
//	@Override
//	public T getRightOperand() {
//		return rightOperand;
//	}
//
//	
//	@Override
//	public String writeSql() {
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append(SQL_OPEN_DELIMITER);
//		for (CompositionNode node : statementsChain) {
//			sb.append(node.predicate.writeSql());
//			if (! (node.operator.equals(ChainingOperator.END))) {
//				sb.append(SQL_SPACE_DELIMITER);
//				sb.append(node.operator.name());
//			}
//		}
//		sb.append(SQL_CLOSE_DELIMITER);
//		
//		return sb.toString();
//	}
//
//	
//	@Override
//	public void chainPredicate(IPredicate<T> predicate, ChainingOperator operator) {
//		checkTerminated();
//		CompositionNode newNode = new CompositionNode(predicate, operator);
//		statementsChain.add(newNode);
//		if(operator.equals(ChainingOperator.END)) {
//			terminated = true;
//		}
//	}
//
//	
//	private void checkTerminated() {
//		if(terminated) {
//			//TODO manage exception properly
//			throw new IllegalStateException();
//		}
//	}
//
//	@Override
//	public Predicate<T> getPredicate() {
//		return new CPredicate(statementsChain);
//	}
//}
