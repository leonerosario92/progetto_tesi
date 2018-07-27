package dataprovisioner;

import java.util.Set;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataProvisioner {

	public IDataSet loadColumnarDataSet(Set<FieldDescriptor> fields) throws DataSourceException;

	public IDataSet loadSingleColumnDataset(FieldDescriptor field) throws DataSourceException;

	public IDataSet loadMaterializedDataSet(Set<FieldDescriptor> columns) throws DataSourceException;

}

