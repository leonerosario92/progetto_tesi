package query.function;

import dataset.IDataSet;
import dataset.IEntity;
import query.operator.IQueryParams;

@FunctionalInterface
public interface IQueryFunction<P extends IQueryParams> {
	
	public IDataSet applyFunction(IDataSet inputSet, P params);
	
}
