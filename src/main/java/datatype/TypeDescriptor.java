package datatype;

import java.util.Optional;

public interface TypeDescriptor {

	public boolean isComparable();
//	public Optional<TypeComparator> getTypeComparator();
//	
	public boolean isAggregable();
//	public Optional<TypeAggregator> getTypeAggregator();
//	
//	public boolean isNumber();
//	public Optional<Number> getValueAsNumber(Object value);
//	
//	public boolean isString();
//	public Optional<String> getValueAsString(Object value);
	
}