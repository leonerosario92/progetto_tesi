package impl.query.execution.operator.filterscan;


import dataset.IDataSet;
import model.FieldDescriptor;
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class FilterScanImpl extends FilterScanFunction{

	@Override
	public IDataSet apply(FilterScanArgs arg0) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public IDataSet apply(FilterScanArgs args) {
//		
//		IDataSet dataSet = args.getInputDataSet();
//		FieldDescriptor f = args.getField();
//		
//		
//		
//		
//		Iterable<String> iterable = () -> dataSet.getColumnIterator(f);
//		Stream<String> targetStream = StreamSupport.stream(iterable.spliterator(), false);
//	}

}
