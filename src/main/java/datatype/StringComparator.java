package datatype;

public class StringComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		String l =  String.class.cast(leftOperand);
		String r =  String.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
