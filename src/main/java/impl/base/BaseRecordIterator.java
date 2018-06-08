package impl.base;

import java.util.ArrayList;
import java.util.Iterator;

import context.DataType;
import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;

public class BaseRecordIterator implements IRecordIterator {
	
	private ArrayList<Iterator<?>> columnIterators;
	private ArrayList<ColumnDescriptor> columnDescriptors;
	private int iterationIndex;
	private int recordCount;
	private int columnCount;
	private Object[] currentRecord;
	
	public BaseRecordIterator(IDataSet dataSet) {
		iterationIndex = 0;
		recordCount = dataSet.getRecordCount();
		columnCount = dataSet.getFieldsCount();
		currentRecord = new Object[columnCount];
		
		columnDescriptors = new ArrayList<>(columnCount);
		columnIterators = new ArrayList<>(columnCount);
		int columnIndex = 0;
		for (IColumn<?> column : dataSet.getAllColumns()) {
			columnIterators.add(columnIndex,column.getColumnIterator());
			columnDescriptors.add(columnIndex, column.getDescriptor());
			columnIndex ++;
		}
		
	}

	@Override
	public boolean hasNext() {
		return iterationIndex < recordCount;
	}

	@Override
	public int getFieldsCount() {
		return columnCount;
	}

	@Override
	public DataType getColumnType(int index) {
		return columnDescriptors.get(index).getColumnType();
	}

	@Override
	public String getColumnName(int index) {
		return columnDescriptors.get(index).getColumnName();
	}

	@Override
	public String getTableName(int index) {
		return columnDescriptors.get(index).getTableName();
	}

	@Override
	public Object getValueByColumnIndex(int index) {
		if (index > recordCount) {
			throw new IndexOutOfBoundsException();
		}
		return currentRecord[index];
	}

	@Override
	public void next() {
		for(int i = 0; i < columnCount; i++) {
			columnIterators.get(i).next();
		}
		iterationIndex ++;
	}

	@Override
	public int getRecordCount() {
		return recordCount;
	}

}
