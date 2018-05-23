package cache;

import java.util.List;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataProvisioner {
	
	public IDataSet loadEntity(TableDescriptor table, IDataSource dataSource, ILayoutManager layoutManager);

	public IDataSet loadEntity(FieldDescriptor field, IDataSource dataSource, ILayoutManager layoutManager);

	public IDataSet loadDataSet(TableDescriptor table, List<FieldDescriptor> fields) throws DataSourceException;

}

