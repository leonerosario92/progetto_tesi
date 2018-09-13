package datasource;

import dataset.IRecordMapper;
import datatype.DataType;
import model.FieldDescriptor;

public interface IRecordScanner {
	
	public boolean next();
	
//	public void resetToFirstRecord();
	
	
	public DataType getColumnType(int index);
	
	public String getColumnName(int index);
	
	public String getTableName(int index);
	
	public int getColumnIndex(FieldDescriptor field);
	
	public Object[] getCurrentRecord ();
	
	public String getColumnId(int index);

	
	public Object getValueByColumnIndex(int index); 
	
	public Object getValueByColumnDescriptor(FieldDescriptor field);
	
	public Object getValueByColumnID(String comulnId);
	
	public IRecordMapper getRecordMapper();
	
	
	public int getFieldsCount();
	
}
