package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import datatype.DataType;

public class BaseRecordIterator implements IRecordIterator {

	private ArrayList<Iterator<?>> columnIterators;
	private ArrayList<ColumnDescriptor> columnDescriptors;
	private int iterationIndex;
	private int recordCount;
	private int columnCount;
	//private Object[] currentRecord;
	private IDataSet dataSet;
	private HashMap <String,Integer> nameIndexMapping;
	
	
	public BaseRecordIterator(BaseDataSet dataSet) {
		this.dataSet = dataSet;
		recordCount = dataSet.getRecordCount();
		columnCount = dataSet.getFieldsCount();
		//currentRecord = new Object[columnCount];
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
	public DataType getColumnType(int index) {
		return columnDescriptors.get(index).getColumnType();
	}

	@Override
	public String getColumnName(int index) {
		if(index < 0 || index >= recordCount) {
			throw new IndexOutOfBoundsException();
		}
		return columnDescriptors.get(index).getColumnName();
	}

	@Override
	public String getTableName(int index) {
		return columnDescriptors.get(index-1).getTableName();
	}

	@Override
	public int getColumnIndex(String columnName) {
		if(! (nameIndexMapping.containsKey(columnName))){
			throw new IllegalArgumentException("Attempt to retrieve index of unknown column.");
		}
		return (nameIndexMapping.get(columnName));
	}


	@Override
	public int getFieldsCount() {
		return columnCount;
	}

	@Override
	public int getRecordCount() {
		return recordCount;
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
