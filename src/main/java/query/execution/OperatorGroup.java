package query.execution;

import java.util.function.Supplier;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import impl.base.BaseQueryExecutor;
import utils.TreePrinter;

public interface OperatorGroup extends ExecutionPlanElement{

	public Supplier<IDataSet> execOperators(IQueryExecutor executor) throws QueryExecutionException;

	public Supplier<IDataSet> execOperators(BaseQueryExecutor baseQueryExecutor, MeasurementType measurement);
	
}

