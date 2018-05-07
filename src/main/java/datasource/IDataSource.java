package datasource;

import dataIterator.ColumnIterator;
import dataIterator.TableIterator;
import memorycache.IMemoryCache;
import model.IMetaData;
import query.IQueryProvider;
import query.QueryBuilder;

public interface IDataSource {
	
	public IMetaData getMetaData ();
	
	public TableIterator getTable(String tableID);
	
	public TableIterator getTable(String tableID,int offset, int recordCount);
	
	public ColumnIterator getColumn(String fieldID);
	
	public ColumnIterator getColumn(String fieldID,int offset, int recordCount);
	
}
