package dispatcher;

import dataset.IRecordIterator;
import datasource.IRecordScanner;
import query.builder.Query;
import query.execution.QueryExecutionException;

public interface IQueryDispatcher {

	public IRecordScanner dispatchQuery(Query query) throws QueryExecutionException;
	
	public IRecordScanner dispatchQuery(Query query, MeasurementType measurementType) throws QueryExecutionException;
	
}
