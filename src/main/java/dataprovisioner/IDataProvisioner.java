package dataprovisioner;

import java.util.Set;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataProvisioner {
	
	public IDataSet loadEntity(TableDescriptor table, IDataSource dataSource, ILayoutManager layoutManager);

	public IDataSet loadEntity(FieldDescriptor field, IDataSource dataSource, ILayoutManager layoutManager);

	public IDataSet loadDataSet(TableDescriptor table, Set<FieldDescriptor> fields) throws DataSourceException;

}

