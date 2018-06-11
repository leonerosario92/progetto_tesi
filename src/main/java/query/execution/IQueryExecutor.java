package query.execution;

import dataset.IDataSet;
import dataset.IEntity;
import dispatcher.MeasurementType;
import impl.query.execution.ExecutionException;
import query.QueryProvider;
import query.builder.Query;

public interface IQueryExecutor {
	 
	public IDataSet executePlan(ExecutionPlan plan) throws ExecutionException;
	
	public IDataSet executePlan(ExecutionPlan plan, Query query, MeasurementType measurement) throws ExecutionException;
	
}