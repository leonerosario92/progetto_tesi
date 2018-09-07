package query.execution;

import dataprovisioner.IDataProvisioner;
import dataset.ILayoutManager;

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
	
	@Override
	public IDataProvisioner getProvisioner() {
		return dataProvisioner;
	}

	@Override
	public ILayoutManager getlayoutManager() {
		return layoutManager;
	}
}
