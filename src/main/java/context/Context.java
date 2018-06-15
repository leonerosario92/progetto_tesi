package context;

import dataprovisioner.IDataProvisioner;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.IDataSource;
import datasource.IRemoteDataSource;
import dispatcher.IQueryDispatcher;
import dispatcher.MeasurementType;
import model.IMetaData;
import query.IQueryPlanner;
import query.ImplementationProvider;
import query.builder.InitialBuilder;
import query.builder.Query;
import query.execution.QueryExecutionException;
import query.execution.IQueryExecutor;

public class Context implements AutoCloseable {
		
	private IDataSource dataSource;
	private IQueryDispatcher queryDispatcher;
	
	
	protected Context (
			IDataSource dataSource, 
			ILayoutManager layoutmanager, 
			IDataProvisioner cachingManager,
			IQueryExecutor queryExecutor,
			ImplementationProvider queryProvider,
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
	
	
	public IRecordIterator executeQuery(Query query) throws QueryExecutionException {
		return queryDispatcher.dispatchQuery(query);
	}
	
	public IRecordIterator executeQuery(Query query, MeasurementType measurememtType) throws QueryExecutionException {
		return queryDispatcher.dispatchQuery(query, measurememtType);
	}


	@Override
	public void close() throws Exception {
		if(dataSource instanceof IRemoteDataSource) {
			dataSource.close();
		}
		
	}
	
}
