package dataset;

import model.ITableDescriptor;

public interface IDataSet {
	
	public IRecordIterator getRecordIterator();
	
	public IColumnIterator getColumnIterator();
	
	public boolean containsField(String fieldName);
}
