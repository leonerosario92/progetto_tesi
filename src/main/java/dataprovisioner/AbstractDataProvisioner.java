package dataprovisioner;

import dataset.ILayoutManager;
import datasource.IDataSource;

public abstract class AbstractDataProvisioner implements IDataProvisioner {
	
	protected IDataSource dataSource;
	protected ILayoutManager layoutManager;
	
	public AbstractDataProvisioner(IDataSource dataSource, ILayoutManager layoutManager) {
		this.dataSource = dataSource;
		this.layoutManager = layoutManager;
	}

}
