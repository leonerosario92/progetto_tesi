package dispatcher;

import dataset.IEntity;

public interface IQueryDispatcher {
	
	public IEntity execQuery(IQuery query);
	
}
