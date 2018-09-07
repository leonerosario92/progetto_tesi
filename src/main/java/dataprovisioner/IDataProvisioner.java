package dataprovisioner;

import java.util.Set;

import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import datasource.IDataSource;
import impl.base.StreamPipeline;
import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.statement.CFNode;

public interface IDataProvisioner {

//	public IDataSet<?> loadColumnarDataSet(Set<FieldDescriptor> fields) throws DataSourceException;
//
//	public IDataSet<?> loadSingleColumnDataset(FieldDescriptor field) throws DataSourceException;

	public IDataSet loadMaterializedDataSet(Set<FieldDescriptor> columns) throws DataSourceException;

	public IDataSet loadFilteredMaterializedDataSet(Set<FieldDescriptor> columns, Set<CFNode> filterStatements)
			throws DataSourceException;

	public StreamPipeline loadStreamedDataSet(Set<FieldDescriptor> columns) throws DataSourceException;

}

