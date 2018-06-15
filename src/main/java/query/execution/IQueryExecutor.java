package query.execution;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dispatcher.MeasurementType;
import query.builder.Query;
import utils.IResultHolder;

public interface IQueryExecutor {
	 
	public IDataSet executePlan(ExecutionPlan plan) throws QueryExecutionException;

	public IDataSet executePlan(ExecutionPlan plan, MeasurementType measurement) throws QueryExecutionException;
	
	IResultHolder<IDataSet> submit (Callable<IDataSet> executable);
	
	public IDataProvisioner getProvisioner();

	public ILayoutManager getlayoutManager();
	
}