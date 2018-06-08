package impl.base;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.MeasurementType;
import dispatcher.QueryDispatcher;
import impl.query.execution.ExecutionException;
import objectexplorer.MemoryMeasurer;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.IQueryExecutor;

public class BaseQueryDispatcher extends QueryDispatcher {
	
	public BaseQueryDispatcher () {
		super ();
	}
	
	public BaseQueryDispatcher getInstance() {
		return new BaseQueryDispatcher();
	}

	@Override
	public IRecordIterator dispatchQuery(Query query) {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		IDataSet result;
		query.setExecutionStartTime();
		try {
			result = executor.executePlan(queryPlan);
			
			long memoryOccupation = MemoryMeasurer.measureBytes(result);
			query.setResultSetByteSize(memoryOccupation);
			
			return result.getRecordIterator();				
		} catch (ExecutionException e) {
			
		}
		finally {
			query.setExecutionEndTime();
		}
		return null;	
	}

	@Override
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws ExecutionException {
		
		IDataSet result = null;
		switch(measurementType) {
		case EVALUATE_PERFORMANCE:
			result = executeWithPerformanceEvaluation(query);
			break;
		case EVALUATE_MEMORY_OCCUPATION:
        			result = executeWithMemoryEvaluation(query);
			break;
		}
		return result.getRecordIterator();
	}

	
	private IDataSet executeWithMemoryEvaluation(Query query) throws ExecutionException {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		
		IDataSet result = null;
		result = executor.executePlan(queryPlan);

		long memoryOccupation = MemoryMeasurer.measureBytes(result);
		query.setResultSetByteSize(memoryOccupation);
		
		return result;	
	}

	
	private IDataSet executeWithPerformanceEvaluation(Query query) throws ExecutionException {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		
		IDataSet result;
		query.setExecutionStartTime();
		
		result = executor.executePlan(queryPlan);
		
		query.setExecutionEndTime();
		
		return result;	
	}
	
	
	
}

