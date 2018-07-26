package datatype;

import java.util.Optional;

public class DoubleDescriptor implements NumericTypeDescriptor{

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public TypeComparator getTypeComparator() {
		return new DoubleComparator();
	}

	@Override
	public boolean isAggregable() {
		return true;
	}

	@Override
	public TypeAggregator getTypeAggregator() {
		return new DoubleAggregator();
	}

	@Override
	public Number getValueAsNumber(Object value) {
		return Double.class.cast(value);
	}

}
