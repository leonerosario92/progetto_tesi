package dataset;

import java.util.Iterator;
import java.util.List;

import datasource.IRecordScanner;


public interface ILayoutManager {

	public IDataSet mergeColumnarDatasets(Iterable<IDataSet> dataSets);

	public IDataSet buildColumnarDataSet(IRecordScanner it);
	
	IDataSet buildMaterializedDataSet(IRecordScanner scanner);

	public IDataSet buildMaterializedDataSet(
			int recordCount, 
			List<ColumnDescriptor> columnSequence, 
			Iterator<Object[]> records
	);

	
}
