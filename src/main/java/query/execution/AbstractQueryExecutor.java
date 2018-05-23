package query.execution;

import cache.IDataProvisioner;
import context.Context;
import dataset.ILayoutManager;

public abstract class AbstractQueryExecutor implements IQueryExecutor {

	protected IDataProvisioner dataProvisioner;
	protected ILayoutManager layoutManager;
	
	public AbstractQueryExecutor(Context context) {
		this.layoutManager = context.getLayoutmanager();
		this.dataProvisioner = Context.getDataProvisioner();
	}
	
	

}
