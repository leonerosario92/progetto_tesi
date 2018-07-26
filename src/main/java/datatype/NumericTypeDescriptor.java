package datatype;

import java.util.Optional;

public interface NumericTypeDescriptor extends AggregableTypeDescriptor {

	public Number getValueAsNumber(Object value);
	
}
