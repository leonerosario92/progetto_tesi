package datatype;

import java.util.Optional;

public class FloatDescriptor implements NumericTypeDescriptor{



	@Override
	public TypeComparator getTypeComparator() {
		return new FloatComparator();
	}


	@Override
	public TypeAggregator<?> getTypeAggregator() {
		return new FloatAggregator();
	}


	@Override
	public Number getValueAsNumber(Object value) {
		return Float.class.cast(value);
	}


	@Override
	public boolean isComparable() {
		return true;
	}


	@Override
	public boolean isAggregable() {
		return true;
	}


}
