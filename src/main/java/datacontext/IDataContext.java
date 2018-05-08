package datacontext;

import memorycache.ICachableEntity;
import model.IMetaData;
import query.QueryBuilder;

public interface IDataContext {
	
	public QueryBuilder query();
	
	public IMetaData getMetadata ();
	
	public ICachableEntity getIfCached(String key);

}
