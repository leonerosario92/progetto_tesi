package context;

import impl.query.execution.operator.filterscan.TypeComparator;

public enum DataType {
	INTEGER,
	LONG,
	DOUBLE,
	FLOAT,
	BIG_DECIMAL,
	STRING;
}
//	TypeComparator comparator;
//	private DataType(Class<? extends TypeComparator> comparatorClass) {
//		try {
//			this.comparator = comparatorClass.newInstance();
//		} catch (InstantiationException | IllegalAccessException e) {
//			//TODO Manage exception properly
//			throw new RuntimeException(e);
//		}
//	}
//	public TypeComparator getComparator() {
//		return comparator;
//	}
//	
//	private class IntegerComparator extends TypeComparator{
//		@Override
//		public int compare(Object leftOperand, Object rightOperand) {
//			Integer l = Integer.class.cast(leftOperand);
//			Integer r = Integer.class.cast(rightOperand);
//			return l.compareTo(r);
//		}
//	}
//}
