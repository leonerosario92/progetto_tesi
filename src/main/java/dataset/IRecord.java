package dataset;

import datatype.DataType;

public interface IRecord {
	
	public DataType getDataType(int columnIndex);
	
	public Object getValue(int columnIndex);
	
}
