package query.execution.operator;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;

public interface Executable <T> {

	public abstract T execute(IQueryExecutor executor) throws QueryExecutionException;
	
	public abstract T execute(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException;
	
}
