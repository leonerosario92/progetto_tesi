package datacontext;

import datasource.IDataSource;
import memorycache.ICachableEntity;
import model.IMetaData;
import query.IQueryBuilder;

public class DataContext implements IDataContext {
	
	private IDataSource dataSource;

	public IQueryBuilder query() {
		
	}

	public IMetaData getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public ICachableEntity getIfCached(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public IDataSource getDataSource() {
	return dataSource;
	}

}
