package dispatcher;

import dataset.IRecordIterator;
import query.builder.Query;
import query.execution.ExecutionException;

public interface IQueryDispatcher {

	public IRecordIterator dispatchQuery(Query query) throws ExecutionException;
	
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws ExecutionException;
	
}
