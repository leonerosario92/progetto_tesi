package query;

import context.Context;
import dataset.IDataSet;
import dataset.IEntity;
import dataset.IRecordIterator;
import query.builder.Query;

public interface IQueryDispatcher {

	public IRecordIterator dispatchQuery(Query query, Context context);
	
}
