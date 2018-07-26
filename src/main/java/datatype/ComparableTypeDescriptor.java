package datatype;

import java.util.Optional;

public interface ComparableTypeDescriptor extends TypeDescriptor{

	public TypeComparator getTypeComparator();
	
}
