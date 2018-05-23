package dataset;

public interface IRecordIterator {
	
	public IRecord getNextRecord();
	
	public boolean hasNext();
		
	public int getFieldsCount();
	
	public Class<?> getColumnType(int index);
	
	public String getColumnName(int index);
	
}
