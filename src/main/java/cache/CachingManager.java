package cache;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface CachingManager {
	
	public int getCacheSize();
	
	public IDataSet loadEntity(TableDescriptor table, IDataSource dataSource, ILayoutManager layoutManager);

	public IDataSet loadEntity(FieldDescriptor field, IDataSource dataSource, ILayoutManager layoutManager);
}

