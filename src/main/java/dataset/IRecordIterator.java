package dataset;

import datatype.DataType;

public interface IRecordIterator {
		
	public boolean hasNext();
		
	public int getFieldsCount();
	
	public DataType getColumnType(int index);
	
	public String getColumnName(int index);
	
	public String getTableName(int index);

	public Object getValueByColumnIndex(int index); 
	
	public Object getValueByColumnName(String columnName);

	public void next();
	
	public void resetToFirstRecord();
	
	public int getRecordCount();
	
}
