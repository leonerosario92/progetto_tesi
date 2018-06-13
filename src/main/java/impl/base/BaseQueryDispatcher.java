package impl.base;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IDataSource;
import dispatcher.MeasurementType;
import dispatcher.QueryDispatcher;
import objectexplorer.MemoryMeasurer;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.ExecutionException;
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
	public IRecordIterator dispatchQuery(Query query) throws ExecutionException {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		IDataSet result;
		result = executor.executePlan(queryPlan);
		return result.getRecordIterator();				
	}

	@Override
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws ExecutionException {
		IDataSet result = null;
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		//result = executor.executePlan(queryPlan, query, measurementType); 
		
		String plan = queryPlan.toString();
		
		query.setExecutionStartTime();
		result = executor.executePlan(queryPlan);
		query.setExecutionEndTime();
		
		
		query.setDataSetLoadingStartTime();
		query.setDataSetLoadingEndTime();
		
		return result.getRecordIterator();
	}

}

