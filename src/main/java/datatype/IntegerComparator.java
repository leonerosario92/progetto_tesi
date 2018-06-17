package datatype;

public class IntegerComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		Integer l = Integer.class.cast(leftOperand);
		Integer r = Integer.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
