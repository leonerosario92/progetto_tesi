package dispatcher;

import dataset.IRecordIterator;
import impl.query.execution.ExecutionException;
import query.builder.Query;

public interface IQueryDispatcher {

	public IRecordIterator dispatchQuery(Query query);
	
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws ExecutionException;
	
}