package dataset;

import context.DataType;

public interface IRecordIterator {
		
	public boolean hasNext();
		
	public int getFieldsCount();
	
	public DataType getColumnType(int index);
	
	public String getColumnName(int index);
	
	public String getTableName(int index);

	public Object getValueAt (int index);
	
}
