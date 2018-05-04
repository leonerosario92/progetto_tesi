package datasource;

import dataIterator.ITableIterator;
import dataIterator.TableIterator;
import memorycache.IMemoryCache;
import model.IMetaData;
import query.IQueryProvider;
import query.QueryBuilder;

public interface IDataSource {
	
	public boolean isCachable();
	
	
	
	public IMetaData getMetaData ();
	
	public TableIterator loadTable();
	
	
	
	//Valutare se definirle tramite costruttore
	
	//Connection nel caso di relational DB
	public void setQueryProvider (IQueryProvider provider);
	public void SetMemoryCache(IMemoryCache cache);
	
	
	
	//implementazione di default restituisce tutto
	//se differenzio dataset/datasource va nel dataset
	public QueryBuilder query();
	
	public 
}
