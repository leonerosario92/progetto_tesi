package query.operator;

import dataset.IDataSet;
import dataset.IEntity;

@FunctionalInterface
public interface IQueryFunction<P extends IQueryParams> {
	
	public IDataSet applyFunction(IDataSet inputSet, P params);
	
}
