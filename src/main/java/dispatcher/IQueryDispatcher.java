package dispatcher;

import dataset.IRecordIterator;
import query.builder.Query;
import query.execution.QueryExecutionException;

public interface IQueryDispatcher {

	public IRecordIterator dispatchQuery(Query query) throws QueryExecutionException;
	
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws QueryExecutionException;
	
}
