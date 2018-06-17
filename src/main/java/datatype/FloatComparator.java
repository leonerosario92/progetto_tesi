package datatype;

public class FloatComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		 Float l =  Float.class.cast(leftOperand);
		 Float r =  Float.class.cast(rightOperand);
		return l.compareTo(r);
	}
}