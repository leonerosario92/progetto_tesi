
package context;


import dataprovisioner.DataProvisioner;
import dataset.LayoutManager;
import datasource.IDataSource;
import dispatcher.QueryDispatcher;
import impl.base.BaseDataProvisioner;
import impl.base.BaseQueryDispatcher;
import impl.base.BaseQueryExecutor;
import impl.base.BaseQueryPlanner;
import impl.query.execution.operator.filterscan.FilterOnColumnImpl;
import impl.base.BaseLayoutManager;
import query.QueryPlanner;
import query.QueryProvider;
import query.execution.QueryExecutor;

public class ContextFactory {
	
	private IDataSource dataSource;
	private QueryProvider queryProvider;

	private Class<? extends LayoutManager> layoutManagerImpl;
	private Class<? extends DataProvisioner> dataProvisionerImpl;
	private Class<? extends QueryExecutor> queryExecutorImpl;
	private Class<? extends QueryPlanner> queryPlannerImpl;
	private Class<? extends QueryDispatcher> queryDispatcherImpl;
	
	private ContextFactory(IDataSource dataSource) {
		
		this.dataSource = dataSource;
		this.queryProvider = new QueryProvider();
		this.queryProvider.setFilterOnColumnImpl(FilterOnColumnImpl.class);
		
		this.layoutManagerImpl = BaseLayoutManager.class;
		this.dataProvisionerImpl = BaseDataProvisioner.class;
		this.queryExecutorImpl = BaseQueryExecutor.class;
		this.queryPlannerImpl = BaseQueryPlanner.class;
		this.queryDispatcherImpl = BaseQueryDispatcher.class;

	}
	
	
	public static ContextFactory getInstance(IDataSource dataSource) {
		return new ContextFactory(dataSource);
	}
	
	
	public Context getContext() throws ContextFactoryException {
		
		if((dataSource == null)) {
			throw new ContextFactoryException ("Error : mandatory fields of contextFactory not set");
		}
		
		LayoutManager layoutManager;
		try {
			layoutManager = layoutManagerImpl.newInstance();
			DataProvisioner dataProvisioner = dataProvisionerImpl.newInstance();
			QueryExecutor queryExecutor = queryExecutorImpl.newInstance();
			QueryPlanner queryPlanner = queryPlannerImpl.newInstance();
			QueryDispatcher queryDispatcher = queryDispatcherImpl.newInstance();
			
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
		} catch (InstantiationException | IllegalAccessException e) {
			//TODO : Manage exception properly
			throw new ContextFactoryException("Error while instantiating Context implementation");
		}
		
		
	}

	
	public void setLayoutManager(Class<? extends LayoutManager> layoutManagerImpl) {
		this.layoutManagerImpl = layoutManagerImpl;
	}

	public void setDataProvisioner(Class<? extends DataProvisioner> dataProvisionerImpl) {
		this.dataProvisionerImpl = dataProvisionerImpl;
	}

	public void setQueryExecutor(Class<? extends QueryExecutor> queryExecutorImpl) {
		this.queryExecutorImpl = queryExecutorImpl;
	}

//	public void setQueryProvider(QueryProvider queryProviderImpl) {
//		this.queryProviderImpl = queryProviderImpl;
//	}

	public void setQueryPlanner(Class<? extends QueryPlanner> queryPlannerImpl) {
		this.queryPlannerImpl = queryPlannerImpl;
	}

	public void setQueryDispatcher(Class<? extends QueryDispatcher> queryDispatcherImpl) {
		this.queryDispatcherImpl = queryDispatcherImpl;
	}
	
}
