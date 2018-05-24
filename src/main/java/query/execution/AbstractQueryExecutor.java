package query.execution;

import dataprovisioner.IDataProvisioner;
import dataset.ILayoutManager;

public abstract class AbstractQueryExecutor implements IQueryExecutor {

	protected IDataProvisioner dataProvisioner;
	protected ILayoutManager layoutManager;
	
	public AbstractQueryExecutor(IDataProvisioner dataProvisioner, ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
		this.dataProvisioner = dataProvisioner;
	}
	

}
