package impl.base;

import java.util.List;

import cache.IDataProvisioner;
import context.Context;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public class BaseDataProvisioner implements IDataProvisioner {
	
	private IDataSource dataSource;
	private ILayoutManager layoutManager;
	
	public BaseDataProvisioner(Context context) {
		this.dataSource = context.getDataSource();
		this.layoutManager = context.getLayoutmanager();
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

	@Override
	public IDataSet loadDataSet(TableDescriptor table, List<FieldDescriptor> fields) throws DataSourceException {
		
		//Ricerca nella cache
		
		FieldDescriptor [] arr = new FieldDescriptor[fields.size()];
		IRecordIterator it = 
				dataSource.getTableProjection(table, fields.toArray(arr));
		IDataSet result = layoutManager.newDataSet(it);
				
		//Merge con i dati nella cache 
		
		return result;
	}

	



}
