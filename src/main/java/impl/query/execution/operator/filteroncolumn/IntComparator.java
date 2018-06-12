package impl.query.execution.operator.filteroncolumn;

public class IntComparator extends TypeComparator {

	@Override
	public int compare(Object leftOperand, Object rightOperand) {
		return ((Integer)leftOperand).compareTo((Integer) rightOperand);
	}

}
