package dataset;

import java.util.List;


public interface ILayoutManager {

	public IDataSet mergeDatasets(List<IDataSet> dataSets);

	public IDataSet buildDataSet(IRecordIterator it);
	
}
