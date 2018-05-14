package dispatcher;

import dataset.IEntity;
import query.builder.Query;

public interface IQueryDispatcher {
	
	public IEntity execQuery(Query query);
	
}
