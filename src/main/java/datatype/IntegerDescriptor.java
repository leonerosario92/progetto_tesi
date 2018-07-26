package datatype;

import java.util.Optional;

public class IntegerDescriptor implements NumericTypeDescriptor {

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public TypeComparator getTypeComparator() {
		return new IntegerComparator();
	}
	@Override
	public boolean isAggregable() {
		return true;
	}

	@Override
	public TypeAggregator getTypeAggregator() {
		return new IntegerAggregator();
	}

	@Override
	public Number getValueAsNumber(Object value) {
		return Integer.class.cast(value);
	}

}
