package dataprovisioner;

import dataset.ILayoutManager;
import datasource.IDataSource;

public abstract class DataProvisioner implements IDataProvisioner {
	
	protected IDataSource dataSource;
	protected ILayoutManager layoutManager;
	
	public DataProvisioner () {}

	public void setDataSource(IDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setLayoutManager(ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

}
