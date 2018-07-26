package datatype;

import java.util.Optional;

public class  StringDescriptor implements ComparableTypeDescriptor{

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public TypeComparator getTypeComparator() {
		return new StringComparator();
	}

	@Override
	public boolean isAggregable() {
		return false;
	}


}
