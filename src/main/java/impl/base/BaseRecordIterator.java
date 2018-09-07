package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IRecordIterator;

public class BaseRecordIterator implements IRecordIterator {

	private ArrayList<Iterator<?>> columnIterators;
	private ArrayList<ColumnDescriptor> columnDescriptors;
	private int iterationIndex;
	private int recordCount;
	private int columnCount;
	private BaseDataSet dataSet;
	private HashMap <String,Integer> nameIndexMapping;
	
	
	public BaseRecordIterator(BaseDataSet dataSet) {
		this.dataSet = dataSet;
		recordCount = dataSet.getRecordCount();
		columnCount = dataSet.getFieldsCount();
		columnDescriptors = new ArrayList<>(columnCount);
		nameIndexMapping = new HashMap<>();
		
		initializeIterators();
	}
	
	
	private void initializeIterators() {
		columnIterators = new ArrayList<>(columnCount);
		iterationIndex = 0;
		int columnIndex = 0;
		for (IColumn<?> column : dataSet.getAllColumns()) {
			columnIterators.add(columnIndex,column.getColumnIterator());
			columnDescriptors.add(columnIndex, column.getDescriptor());
			nameIndexMapping.put(column.getDescriptor().getColumnName(), columnIndex);
			columnIndex ++;
		}
	}
	
	
	@Override
	public boolean hasNext() {
		return (iterationIndex < recordCount);
	}


	@Override
	public Object[] next() {
		Object[] currentRecord = new Object[columnCount];
		for(int i = 0; i < columnCount; i++) {
			currentRecord[i] = columnIterators.get(i).next();
		}
		iterationIndex ++;
		return currentRecord;
	}
	
}
