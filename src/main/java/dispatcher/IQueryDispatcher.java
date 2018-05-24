package dispatcher;

import dataset.IRecordIterator;
import query.builder.Query;

public interface IQueryDispatcher {

	public IRecordIterator dispatchQuery(Query query);
	
}
