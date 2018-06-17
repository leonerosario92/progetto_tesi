package datatype;

import java.math.BigDecimal;

public class BooleanComparator implements TypeComparator {
	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		Boolean l =  Boolean.class.cast(leftOperand);
		Boolean r =  Boolean.class.cast(rightOperand);
		return l.compareTo(r);
	}
}
