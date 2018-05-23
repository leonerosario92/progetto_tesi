package context;

import cache.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.DataSourceException;
import datasource.IDataSource;
import model.FieldDescriptor;
import model.IMetaData;
import model.TableDescriptor;
import query.IQueryDispatcher;
import query.IQueryPlanner;
import query.QueryProvider;
import query.builder.InitialBuilder;
import query.builder.Query;
import query.execution.ExecutionPlanBlock;
import query.execution.IQueryExecutor;

public class Context {
		
	private IDataSource dataSource;
	private ILayoutManager layoutManager;
	private IDataProvisioner cachingManager;
	private IQueryExecutor queryExecutor;
	private QueryProvider queryProvider;
	private IQueryPlanner queryPlanner;
	private IQueryDispatcher queryDispatcher;
	
	
	protected Context (
			IDataSource dataSource, 
			ILayoutManager layoutmanager, 
			IDataProvisioner cachingManager,
			IQueryExecutor queryExecutor,
			QueryProvider queryProvider,
			IQueryPlanner queryOptimizer,
			IQueryDispatcher queryDispatcher
			) {
		this.dataSource = dataSource;
		this.layoutManager = layoutmanager;
		this.cachingManager = cachingManager;
		this.queryExecutor = queryExecutor;
		this.queryPlanner = queryOptimizer;
		this.queryDispatcher = queryDispatcher;
	}

	
	public InitialBuilder query() {
		return new InitialBuilder(this);
	}

	
	public IMetaData getMetadata() {
		return dataSource.getMetaData();
	}

	
	public IDataSource getDataSource() {
		return dataSource;
	}
	
	
	public IRecordIterator executeQuery(Query query) {
		return queryDispatcher.dispatchQuery(query, this);
	}
	
	
	public IDataSet loadTable(TableDescriptor table) throws DataSourceException {
		return cachingManager.loadEntity(table,dataSource,layoutManager);
	}


	public ILayoutManager getLayoutmanager() {
		return layoutManager;
	}


	public IQueryPlanner getQueryPlanner() {
		return queryPlanner;
	}


	public QueryProvider getQueryProvider() {
		return queryProvider;
	}

}
