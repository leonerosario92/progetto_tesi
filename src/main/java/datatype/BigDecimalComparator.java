package datatype;

import java.math.BigDecimal;

public class BigDecimalComparator implements TypeComparator{
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		BigDecimal l =  BigDecimal.class.cast(leftOperand);
		BigDecimal r =  BigDecimal.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
