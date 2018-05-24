
package context;


import dataprovisioner.IDataProvisioner;
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

import query.IQueryPlanner;
import query.QueryProvider;
import query.execution.IQueryExecutor;

public class ContextFactory {
	
	private IDataSource dataSource;
	private ILayoutManager layoutManager;
	private IDataProvisioner dataProvisioner;
	private IQueryExecutor queryExecutor;
	private QueryProvider queryProvider;
	private IQueryPlanner queryPlanner;
//	private IQueryDispatcher queryDispatcher;
	
	private Class<? extends AbstractQueryDispatcher> queryDispatcherImpl;
	
	private ContextFactory(IDataSource dataSource) {
		
		this.dataSource = dataSource;
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
				
		IQueryDispatcher queryDispatcher;
		if(queryDispatcherImpl == null) {
			queryDispatcherImpl = BaseQueryDispatcher.class;
		}
		queryDispatcher = queryDispatcherImpl.newInstance().
		
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

	
	public void setLayoutManager(ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	public void setCachingManager(IDataProvisioner dataProvisioner) {
		this.dataProvisioner = dataProvisioner;
	}

	public void setQueryExecutor(IQueryExecutor queryExecutor) {
		this.queryExecutor = queryExecutor;
	}

	public void setQueryProvider(QueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}

	public void setQueryPlanner(IQueryPlanner queryPlanner) {
		this.queryPlanner = queryPlanner;
	}

	public void setQueryDispatcher(Class<? extends AbstractQueryDispatcher> queryDispatcherImpl) {
		this.queryDispatcherImpl = queryDispatcherImpl;
	}
	
}
