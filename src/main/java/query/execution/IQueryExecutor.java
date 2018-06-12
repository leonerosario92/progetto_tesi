package query.execution;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dispatcher.MeasurementType;
import impl.query.execution.ExecutionException;
import query.builder.Query;

public interface IQueryExecutor {
	 
	public IDataSet executePlan(ExecutionPlan plan) throws ExecutionException;

//	public IDataSet executePlan(ExecutionPlan plan, Query query, MeasurementType measurement) throws ExecutionException;
	
	public Future<IDataSet> execFunction(Callable<IDataSet> executable);
	
	public IDataProvisioner getProvisioner();

	public ILayoutManager getlayoutManager();

	
}