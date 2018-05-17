package datasource;

import dataset.IColumnIterator;
import dataset.IDataSet;
import dataset.IRecordIterator;
import impl.dataset.jdbc.JDBCDataSourceException;
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;

public interface IDataSource extends AutoCloseable {
	
	public IMetaData getMetaData ();
	
	public IRecordIterator getTable(TableDescriptor table) throws DataSourceException;
	
	//public IRecordIterator getTablePartition(TableDescriptor table,int offset, int recordCount);
	
	public IColumnIterator getColumn(FieldDescriptor field) throws DataSourceException;
	
	//public ColumnIterator getColumnPartition(FieldDescriptor field,int offset, int recordCount);
	
}
