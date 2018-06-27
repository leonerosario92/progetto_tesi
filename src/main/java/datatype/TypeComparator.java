package datatype;

import java.util.Comparator;

public interface TypeComparator extends Comparator<Object>{

	public int compare(Object leftOperand, Object rightOperand);
	
}
