package datatype;

import java.util.Optional;

public class  StringDescriptor implements TypeDescriptor{

	@Override
	public boolean isComparable() {
		return true;
	}

	@Override
	public Optional<TypeComparator> getTypeComparator() {
		return Optional.of( new StringComparator());
	}

	@Override
	public boolean isAggregable() {
		return false;
	}

	@Override
	public Optional<TypeAggregator> getTypeAggregator() {
		return Optional.empty();
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public Optional<Number> getValueAsNumber(Object value) {
		return Optional.empty();
	}

	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public  Optional<String> getValueAsString(Object value) {
		return Optional.of(String.class.cast(value));
	}
}
