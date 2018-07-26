package datatype;

import java.util.Optional;

public class LongDescriptor implements NumericTypeDescriptor{

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public TypeComparator getTypeComparator() {
		return new LongComparator();
	}

	@Override
	public boolean isAggregable() {
		return true;
	}

	@Override
	public TypeAggregator<?> getTypeAggregator() {
		return new LongAggregator();
	}

	@Override
	public Number getValueAsNumber(Object value) {
		return Long.class.cast(value);
	}

}
