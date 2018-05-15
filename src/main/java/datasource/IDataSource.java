package datasource;

import dataset.ColumnIterator;
import dataset.IRecordIterator;
import model.IMetaData;

public interface IDataSource {
	
	public IMetaData getMetaData ();
	
	public IRecordIterator getTable(String tableID);
	
	public IRecordIterator getTable(String tableID,int offset, int recordCount);
	
	public ColumnIterator getColumn(String fieldID);
	
	public ColumnIterator getColumn(String fieldID,int offset, int recordCount);
	
}
