package context;

import dataprovisioner.IDataProvisioner;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.IQueryDispatcher;
import model.IMetaData;
import query.IQueryPlanner;
import query.QueryProvider;
import query.builder.InitialBuilder;
import query.builder.Query;
import query.execution.IQueryExecutor;

public class Context {
		
	private IDataSource dataSource;
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
		return queryDispatcher.dispatchQuery(query);
	}
	
}
