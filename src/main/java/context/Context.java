package context;

import cache.ICachableEntity;
import datasource.IDataSource;
import model.IMetaData;
import query.QueryProvider;
import query.builder.QueryBuilder;

public class Context {

	public QueryBuilder query() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMetaData getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICachableEntity getIfCached(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryProvider getQueryProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
