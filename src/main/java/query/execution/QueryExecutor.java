package query.execution;

import dataprovisioner.DataProvisioner;
import dataprovisioner.IDataProvisioner;
import dataset.ILayoutManager;
import dataset.LayoutManager;

public abstract class QueryExecutor implements IQueryExecutor {

	protected IDataProvisioner dataProvisioner;
	protected ILayoutManager layoutManager;
	
	public QueryExecutor() {}

	public void setDataProvisioner(IDataProvisioner dataProvisioner) {
		this.dataProvisioner = dataProvisioner;
	}

	public void setLayoutManager(ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}
	

}
