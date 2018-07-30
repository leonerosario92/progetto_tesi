package dataset;

import java.util.Iterator;
import java.util.List;

import datasource.IRecordScanner;
import utils.RecordEvaluator;


public interface ILayoutManager {

	public IDataSet mergeColumnarDatasets(Iterable<IDataSet> dataSets);

	public IDataSet buildColumnarDataSet(IRecordScanner it);
	
	public IDataSet buildMaterializedDataSet(IRecordScanner scanner, RecordEvaluator evaluator);

	public IDataSet buildMaterializedDataSet(IRecordScanner recordScanner);
	
	public IDataSet buildMaterializedDataSet(
			int recordCount, 
			List<ColumnDescriptor> columnSequence, 
			Iterator<Object[]> records
	);

}
