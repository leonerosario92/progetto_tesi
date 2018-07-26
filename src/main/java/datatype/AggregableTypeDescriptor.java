package datatype;

import java.util.Optional;

public interface AggregableTypeDescriptor extends ComparableTypeDescriptor{

	public TypeAggregator<?> getTypeAggregator();
	
}
