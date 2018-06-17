package datatype;


public enum DataType {
	BOOLEAN(new BooleanComparator()),
	BYTE(new ByteComparator()),
	SHORT(new ShortComparator()),
	INTEGER(new IntegerComparator()),
	LONG(new LongComparator()),
	DOUBLE(new DoubleComparator()),
	FLOAT(new FloatComparator()),
	BIG_DECIMAL(new BigDecimalComparator()),
	STRING(new StringComparator());
	
	private TypeComparator comparator;
	private DataType(TypeComparator comparator) {
		this.comparator = comparator;
	}
	
	public TypeComparator getComparator() {
		return this.comparator;
	}
}
