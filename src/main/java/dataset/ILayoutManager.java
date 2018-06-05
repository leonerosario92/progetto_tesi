package dataset;

import java.util.Set;


public interface ILayoutManager {

	public IDataSet mergeDatasets(Set<IDataSet> dataSets);

	public IDataSet buildDataSet(IRecordIterator it);
	
}
