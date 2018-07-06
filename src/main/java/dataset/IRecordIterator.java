package dataset;

import java.util.Iterator;

import datatype.DataType;

public interface IRecordIterator extends Iterator<Object[]> {
	
	
	public DataType getColumnType(int index);
	
	public String getColumnName(int index);
	
	public String getTableName(int index);
	
	public int getColumnIndex(String columnName);

	
	public int getFieldsCount();
	
	public int getRecordCount();

	
	
}
