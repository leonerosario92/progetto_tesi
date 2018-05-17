package impl.base;

import cache.CachingManager;
import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public class BaseCachingManager implements CachingManager {

	@Override
	public int getCacheSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IDataSet loadEntity(TableDescriptor table, IDataSource dataSource, ILayoutManager layoutManager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataSet loadEntity(FieldDescriptor field, IDataSource dataSource, ILayoutManager layoutManager) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
