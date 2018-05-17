package query.execution;

import dataset.IDataSet;

public interface IExecutableSet {
	
	public IDataSet execute (IQueryExecutor executor, IDataSet...inputSets);
	
}
