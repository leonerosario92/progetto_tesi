package context;

import cache.CachingManager;
import dataset.ILayoutManager;
import datasource.IDataSource;
import impl.base.BaseCachingManager;
import impl.base.BaseQueryDisapatcher;
import impl.base.BaseQueryExecutor;
import impl.base.BaseQueryPlanner;
import impl.base.BaseQueryProvider;
import impl.base.BaselayoutManager;
import query.IQueryDispatcher;
import query.IQueryPlanner;
import query.QueryProvider;
import query.execution.IQueryExecutor;

public class ContextFactory {
	
	private IDataSource dataSource;
	private ILayoutManager layoutManager;
	private CachingManager cachingManager;
	private IQueryExecutor queryExecutor;
	private QueryProvider queryProvider;
	private IQueryPlanner queryPlanner;
	private IQueryDispatcher queryDispatcher;
	
	private ContextFactory(IDataSource dataSource) {
		
		this.dataSource = dataSource;
		
		layoutManager = new BaselayoutManager();
		cachingManager = new BaseCachingManager();
		queryExecutor = new BaseQueryExecutor();
		queryPlanner = new BaseQueryPlanner();
		queryProvider = new BaseQueryProvider();
		queryDispatcher = new BaseQueryDisapatcher();
	}
	
	public static ContextFactory getInstance(IDataSource dataSource) {
		return new ContextFactory(dataSource);
	}
	
	
	public Context getContext() throws FactoryException {
		
		if((dataSource == null) || (queryDispatcher == null)) {
			throw new FactoryException ("Error : mandatory fields of contextFactory not set");
		}
		
		return new Context(
				dataSource,
				layoutManager,
				cachingManager,
				queryExecutor,
				queryProvider,
				queryPlanner,
				queryDispatcher
				);
	}

	
	public void setLayoutManager(ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	public void setCachingManager(CachingManager cachingManager) {
		this.cachingManager = cachingManager;
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
