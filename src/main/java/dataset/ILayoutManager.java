package dataset;

import java.util.Iterator;
import java.util.List;

import datasource.IRecordScanner;


public interface ILayoutManager {

	public IDataSet mergeDatasets(Iterable<IDataSet> dataSets);

	public IDataSet buildDataSet(IRecordScanner it);

	public IDataSet buildDataSet(
			int recordCount, 
			List<ColumnDescriptor> columnSequence, 
			Iterator<Object[]> records
	);
	
}
