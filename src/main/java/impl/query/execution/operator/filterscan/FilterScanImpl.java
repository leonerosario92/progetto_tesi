package impl.query.execution.operator.filterscan;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class FilterScanImpl extends FilterScanFunction{

	@Override
	public IDataSet apply(FilterScanArgs args) {
		
		IDataSet dataSet = args.getInputDataSet();
		FieldDescriptor f = args.getField();
		
		
		
		
		Iterable<String> iterable = () -> dataSet.getColumnIterator(f);
		Stream<String> targetStream = StreamSupport.stream(iterable.spliterator(), false);
	}

}
