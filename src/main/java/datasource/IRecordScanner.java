package datasource;

import datatype.DataType;

public interface IRecordScanner {
	
	public boolean next();
	
	public void resetToFirstRecord();
	
	
	public DataType getColumnType(int index);
	
	public String getColumnName(int index);
	
	public String getTableName(int index);
	
	public int getColumnIndex(String columnName);
	
	public Object[] getCurrentRecord ();
	
		
	public Object getValueByColumnIndex(int index); 
	
	public Object getValueByColumnName(String columnName);

	
	public int getFieldsCount();
	
	public int getRecordCount();


}
