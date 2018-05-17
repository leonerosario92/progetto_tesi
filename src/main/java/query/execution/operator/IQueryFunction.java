package query.execution.operator;

import dataset.IDataSet;
import dataset.IEntity;

@FunctionalInterface
public interface IQueryFunction<P extends IQueryParams> {
	
	public IDataSet applyFunction(P params, IDataSet...inputSets);
	
}
