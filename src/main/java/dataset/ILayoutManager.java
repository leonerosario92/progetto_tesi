package dataset;

import java.util.List;

import datasource.IRecordScanner;


public interface ILayoutManager {

	public IDataSet mergeDatasets(Iterable<IDataSet> dataSets);

	public IDataSet buildDataSet(IRecordScanner it);
	
}
