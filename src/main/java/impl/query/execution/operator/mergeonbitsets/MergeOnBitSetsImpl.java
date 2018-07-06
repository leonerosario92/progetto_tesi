package impl.query.execution.operator.mergeonbitsets;

import dataset.IDataSet;
import dataset.ILayoutManager;
import query.execution.operator.mergeonbitsets.MergeOnBitSetsArgs;
import query.execution.operator.mergeonbitsets.MergeOnBitSetsFunction;

public class MergeOnBitSetsImpl extends MergeOnBitSetsFunction  {

	@Override
	public IDataSet apply(Iterable<IDataSet> inputDataSets, ILayoutManager layoutManager, MergeOnBitSetsArgs args) {
		return layoutManager.mergeDatasets(inputDataSets);
	}

}
