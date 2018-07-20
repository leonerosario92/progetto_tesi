package datatype;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;

public enum DataType {
	BOOLEAN(null),
	BYTE(null),
	SHORT(null),
	INTEGER(new IntegerDescriptor()),
	LONG(new LongDescriptor()),
	DOUBLE(new DoubleDescriptor()),
	FLOAT(new FloatDescriptor()),
	BIG_DECIMAL(new BigDecimalDescriptor()),
	STRING(new StringDescriptor());
	
	private TypeDescriptor descriptor;
	private DataType(TypeDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public TypeComparator getComparator() {
		return this.descriptor.getTypeComparator().get();
	}
	
	public TypeDescriptor getDescriptor() {
		return this.descriptor;
	}
	
}


