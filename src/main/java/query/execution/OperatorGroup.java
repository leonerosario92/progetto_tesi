package query.execution;

import dataset.IDataSet;
import dispatcher.MeasurementType;

public interface OperatorGroup extends ExecutionPlanElement{

	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException;

	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException;
	
}

 