package datatype;

public class ByteComparator  implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		Byte l = Byte.class.cast(leftOperand);
		Byte r = Byte.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
