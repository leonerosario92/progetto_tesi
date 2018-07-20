package datatype;

import java.util.Optional;

public class FloatDescriptor implements TypeDescriptor{

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public Optional<TypeComparator> getTypeComparator() {
		return Optional.of(new FloatComparator());
	}

	@Override
	public boolean isAggregable() {
		return true;
	}

	@Override
	public Optional<TypeAggregator> getTypeAggregator() {
		return Optional.of(new FloatAggregator());
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public Optional<Number> getValueAsNumber(Object value) {
		return Optional.of(Float.class.cast(value));
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public Optional<String> getValueAsString(Object value) {
		return Optional.empty();
	}
	
}
