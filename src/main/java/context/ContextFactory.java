package context;

import cache.IDataProvisioner;
import dataset.ILayoutManager;
import datasource.IDataSource;
import impl.base.BaseDataProvisioner;
import impl.base.BaseQueryDispatcher;
import impl.base.BaseQueryExecutor;
import impl.base.BaseQueryPlanner;
import impl.base.BaseQueryProvider;
import impl.base.BaseLayoutManager;
import query.IQueryDispatcher;
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
	private IQueryDispatcher queryDispatcher;
	
	private ContextFactory(IDataSource dataSource) {
		
		this.dataSource = dataSource;
		
		layoutManager = new BaseLayoutManager();
		dataProvisioner = new BaseDataProvisioner();
		queryExecutor = new BaseQueryExecutor();
		queryPlanner = new BaseQueryPlanner();
		queryProvider = new BaseQueryProvider();
		queryDispatcher = new BaseQueryDispatcher();
	}
	
	public static ContextFactory getInstance(IDataSource dataSource) {
		return new ContextFactory(dataSource);
	}
	
	
	public Context getContext() throws ContextFactoryException {
		
		if((dataSource == null) || (queryDispatcher == null)) {
			throw new ContextFactoryException ("Error : mandatory fields of contextFactory not set");
		}
		
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

	public void setQueryDispatcher(IQueryDispatcher queryDispatcher) {
		this.queryDispatcher = queryDispatcher;
	}
	
}
