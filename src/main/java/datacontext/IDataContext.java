package datacontext;

import datasource.IDataSource;
import memorycache.ICachableEntity;
import model.IMetaData;
import query.IQueryBuilder;
import query.IQueryProvider;

public interface IDataContext {
	
	public IQueryBuilder query();
	
	public IMetaData getMetadata ();
	
	public IDataSource getDataSource();
	
	public ICachableEntity getIfCached(String key);
	
	public IQueryProvider getQueryProvider();

}
