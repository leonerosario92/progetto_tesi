package datatype;

public class LongComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		Long l = Long.class.cast(leftOperand);
		Long r = Long.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
