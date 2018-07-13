package query.execution;

import java.util.function.Supplier;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import impl.base.BaseQueryExecutor;
import utils.ExecutionPlanNavigator;
import utils.report.OperatorGroupReport;

public interface OperatorGroup extends ExecutionPlanElement{

	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException;

	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException;
	
}

 