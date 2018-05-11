package context;

import cache.ICachableEntity;
import dataset.IEntity;
import datasource.IDataSource;
import model.IMetaData;
import query.IQuery;
import query.IQueryBuilder;
import query.IQueryProvider;

public class Context {

	public IQueryBuilder query() {
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

	public IQueryProvider getQueryProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public IEntity execQuery(IQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
