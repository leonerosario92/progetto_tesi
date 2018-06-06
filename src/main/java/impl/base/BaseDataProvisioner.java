package impl.base;

import java.util.Set;

import dataprovisioner.DataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public class BaseDataProvisioner extends DataProvisioner  {
	
	public BaseDataProvisioner() {
		super();
	}

	
	@Override
	public IDataSet loadEntity(TableDescriptor table, IDataSource dataSource, ILayoutManager layoutManager) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public IDataSet loadEntity(FieldDescriptor field) throws DataSourceException {
	// Here will be performed the search of requested data in the cache
		
		//Retrieve from dataSource the data that has not been found in the cache
		
		IRecordIterator it = 
				dataSource.getTableProjection(field.getTable(), field);
		IDataSet result = layoutManager.buildDataSet(it);
				
		//Merge data found in cache with data retrieved from dataSource 
		
		return result;
	}

	
	@Override
	public IDataSet loadDataSet(TableDescriptor table, Set<FieldDescriptor> fields) throws DataSourceException {
		
		// Here will be performed the search of requested data in the cache
		
		//Retrieve from dataSource the data that has not been found in the cache
		FieldDescriptor [] arr = new FieldDescriptor[fields.size()];
		IRecordIterator it = 
				dataSource.getTableProjection(table, fields.toArray(arr));
		IDataSet result = layoutManager.buildDataSet(it);
				
		//Merge data found in cache with data retrieved from dataSource 
		
		return result;
	}

	



}
