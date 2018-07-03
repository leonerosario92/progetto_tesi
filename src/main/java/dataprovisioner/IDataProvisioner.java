package dataprovisioner;

import java.util.Set;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataProvisioner {

	public IDataSet loadColumns(Set<FieldDescriptor> fields) throws DataSourceException;

	public IDataSet loadColumn(FieldDescriptor field) throws DataSourceException;

}

