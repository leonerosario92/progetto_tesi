
package context;


import dataprovisioner.AbstractDataProvisioner;
import dataprovisioner.IDataProvisioner;
import dataset.AbstractLayoutManager;
import dataset.ILayoutManager;
import datasource.IDataSource;
import dispatcher.AbstractQueryDispatcher;
import dispatcher.IQueryDispatcher;
import impl.base.BaseDataProvisioner;
import impl.base.BaseQueryDispatcher;
import impl.base.BaseQueryExecutor;
import impl.base.BaseQueryPlanner;
import impl.query.execution.operator.filterscan.StreamFilterScan;
import impl.base.BaseLayoutManager;
import query.AbstractQueryPlanner;
import query.IQueryPlanner;
import query.QueryProvider;
import query.execution.AbstractQueryExecutor;
import query.execution.IQueryExecutor;

public class ContextFactory {
	
	private IDataSource dataSource;
	private QueryProvider queryProvider;

	private Class<? extends AbstractLayoutManager> layoutManagerImpl;
	private Class<? extends AbstractDataProvisioner> dataProvisionerImpl;
	private Class<? extends AbstractQueryExecutor> queryExecutorImpl;
	private Class<? extends AbstractQueryPlanner> queryPlannerImpl;
	private Class<? extends AbstractQueryDispatcher> queryDispatcherImpl;
	
	private ContextFactory(IDataSource dataSource) {
		
		this.dataSource = dataSource;
		this.queryProvider = new QueryProvider();
		this.queryProvider.setFilterScanImpl(new StreamFilterScan().getClass());
		
		this.layoutManagerImpl = BaseLayoutManager.class;
		this.dataProvisionerImpl = BaseDataProvisioner.class;
		this.queryExecutorImpl = BaseQueryExecutor.class;
		this.queryPlannerImpl = BaseQueryPlanner.class;
		this.queryDispatcherImpl = BaseQueryDispatcher.class;
		
//		layoutManager = new BaseLayoutManager();
//		
//		dataProvisioner = new BaseDataProvisioner(dataSource,layoutManager);
//		queryExecutor = new BaseQueryExecutor(dataProvisioner,layoutManager);
//		
//		queryProvider = new QueryProvider();
//		queryProvider.setFilterScanImpl(new StreamFilterScan().getClass());
//		queryPlanner = new BaseQueryPlanner(queryProvider);
//		
		
	}
	
	
	public static ContextFactory getInstance(IDataSource dataSource) {
		return new ContextFactory(dataSource);
	}
	
	
	public Context getContext() throws ContextFactoryException {
		
		if((dataSource == null)) {
			throw new ContextFactoryException ("Error : mandatory fields of contextFactory not set");
		}
		
		ILayoutManager  layoutManager = layoutManagerImpl.newInstance();
		IDataProvisioner dataProvisioner = dataProvisionerImpl.newInstance();
		IQueryExecutor queryExecutor = queryExecutorImpl.newInstance();
		IQueryPlanner queryPlanner = queryPlannerImpl.newInstance();
		IQueryDispatcher queryDispatcher = queryDispatcherImpl.newInstance();
		
		dataProvisioner.setDataSource(dataSource);
		dataProvisioner.setLayoutManager(layoutManager);
		
		queryExecutor.setDataProvisioner(dataProvisioner);
		queryExecutor.setLayoutManager(layoutManager);
		
		queryPlanner.setQueryProvider(queryProvider);
		
		queryDispatcher.setDataSource(dataSource);
		queryDispatcher.setQueryPlanner(queryPlanner);
		queryDispatcher.setQueryExecutor(queryExecutor);
		
		return new Context(
				dataSource,
				layoutManager,
				dataProvisioner,
				queryExecutor,
				queryProvider,
				queryPlanner,
				queryDispatcher
				);
	}

	
	public void setLayoutManager(Class<? extends AbstractLayoutManager> layoutManagerImpl) {
		this.layoutManagerImpl = layoutManagerImpl;
	}

	public void setDataProvisioner(Class<? extends AbstractDataProvisioner> dataProvisionerImpl) {
		this.dataProvisionerImpl = dataProvisionerImpl;
	}

	public void setQueryExecutor(Class<? extends AbstractQueryExecutor> queryExecutorImpl) {
		this.queryExecutorImpl = queryExecutorImpl;
	}

//	public void setQueryProvider(QueryProvider queryProviderImpl) {
//		this.queryProviderImpl = queryProviderImpl;
//	}

	public void setQueryPlanner(Class<? extends AbstractQueryPlanner> queryPlannerImpl) {
		this.queryPlannerImpl = queryPlannerImpl;
	}

	public void setQueryDispatcher(Class<? extends AbstractQueryDispatcher> queryDispatcherImpl) {
		this.queryDispatcherImpl = queryDispatcherImpl;
	}
	
}
