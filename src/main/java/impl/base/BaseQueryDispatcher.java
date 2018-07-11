package impl.base;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IDataSource;
import datasource.IRecordScanner;
import dispatcher.MeasurementType;
import dispatcher.QueryDispatcher;
import objectexplorer.MemoryMeasurer;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.QueryExecutionException;
import utils.report.ExecutionReport;
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
	public IRecordScanner dispatchQuery(Query query) throws QueryExecutionException {
		
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		IDataSet result;
		result = executor.executePlan(queryPlan);
		return result.getRecordScanner();				
	}

	@Override
	public IRecordScanner dispatchQuery(Query query, MeasurementType measurementType) throws QueryExecutionException {
		IDataSet result = null;
		ExecutionPlan queryPlan = planner.getExecutionPlan(query);
		String plan = queryPlan.toString();
		
		result = executor.executePlan(queryPlan,measurementType);
		ExecutionReport report = queryPlan.getExecutionReport();
		query.setExecutionTime(report.getExecutionTimeMs());
		query.setMemoryOccupation(report.getMemoryOccupationMB());
		query.setExecutionReport(queryPlan.printReport());
		
		IRecordScanner rs = result.getRecordScanner();
		return rs;
	}

}

