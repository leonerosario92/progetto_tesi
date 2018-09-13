package dataset;

import java.util.Iterator;
import java.util.List;

import datasource.IRecordScanner;
import datasource.ISizedRecordScanner;
import impl.base.BaseDataSet;
import impl.base.StreamedDataSet;
import utils.RecordEvaluator;


public interface ILayoutManager {

//	public IDataSet<Object[]> mergeColumnarDatasets(Iterable<BaseDataSet> dataSets);

//	public IDataSet<Object[]> buildColumnarDataSet(ISizedRecordScanner it);
	
	public IDataSet buildMaterializedDataSet(IRecordScanner scanner, RecordEvaluator evaluator);

	public IDataSet buildMaterializedDataSet(IRecordScanner recordScanner);
	
	public IDataSet buildMaterializedDataSet(
			List<ColumnDescriptor> columnSequence, 
			Iterator<Object[]> records
	);
	
	public StreamedDataSet buildStreamedDataSet(IRecordScanner scanner);

}
