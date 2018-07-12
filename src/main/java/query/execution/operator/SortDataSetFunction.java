package query.execution.operator;

import dataset.IDataSet;
import dataset.ILayoutManager;

public interface SortDataSetFunction<A> extends IOperatorFunction {
	
	public IDataSet apply(IDataSet inputDataSets, ILayoutManager layoutManager, A args);
	
}
