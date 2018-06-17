package datatype;

public class DoubleComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		Double l = Double.class.cast(leftOperand);
		Double r = Double.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
