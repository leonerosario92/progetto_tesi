package query.execution.operator;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import query.execution.operator.mergeonbitsets.MergeOnBitSetsArgs;

public interface MaterializationFunction<A> extends ISequentialOperatorFunction {
	
	public IDataSet apply(Iterable<IDataSet> inputDataSets, ILayoutManager layoutManager, A args);

}
