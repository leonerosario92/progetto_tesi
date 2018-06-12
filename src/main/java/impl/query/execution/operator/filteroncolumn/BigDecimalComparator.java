package impl.query.execution.operator.filteroncolumn;

import java.math.BigDecimal;

public class BigDecimalComparator extends TypeComparator {

	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		BigDecimal lop = BigDecimal.class.cast(leftOperand);
		BigDecimal rop = BigDecimal.class.cast(rightOperand);
		return (lop.compareTo(rop));
	}
	
}
