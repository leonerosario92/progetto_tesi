package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IRecordScanner;
import datatype.DataType;
import model.FieldDescriptor;

public class BaseRecordScanner implements IRecordScanner {
	
	private ArrayList<Iterator<?>> columnIterators;
	private ArrayList<ColumnDescriptor> columnDescriptors;
	private int iterationIndex;
	private int recordCount;
	private int columnCount;
	private Object[] currentRecord;
	private BaseDataSet dataSet;
	private HashMap <String,Integer> nameIndexMapping;
	
	
	public BaseRecordScanner(BaseDataSet dataSet) {
		this.dataSet = dataSet;
		recordCount = dataSet.getRecordCount();
		columnCount = dataSet.getFieldsCount();
		currentRecord = new Object[columnCount];
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

	
	private boolean hasNext() {
		return iterationIndex < recordCount;
	}
	
	@Override
	public boolean next() {
		if( ! hasNext()) {
			return false;
		}
		for(int i = 0; i < columnCount; i++) {
			currentRecord[i] = columnIterators.get(i).next();
		}
		iterationIndex ++;
		return true;
	}

	@Override
	public int getFieldsCount() {
		return columnCount;
	}

	@Override
	public DataType getColumnType(int index) {
		return columnDescriptors.get(index-1).getColumnType();
	}

	@Override
	public String getColumnName(int index) {
		if(index <= 0 || index > recordCount) {
			throw new IndexOutOfBoundsException();
		}
		return columnDescriptors.get(index-1).getColumnName();
	}

	@Override
	public String getTableName(int index) {
		return columnDescriptors.get(index-1).getTableName();
	}

	@Override
	public Object getValueByColumnIndex(int index) {
		if (index > columnCount) {
			throw new IndexOutOfBoundsException();
		}
		return currentRecord[index-1];
	}


	@Override
	public Object getValueByColumnDescriptor(FieldDescriptor field) {
		int index = getColumnIndex(field);
		return getValueByColumnIndex(index);
	}


	@Override
	public int getColumnIndex(FieldDescriptor field) {
		String columnName = field.getName();
		if(! (nameIndexMapping.containsKey(columnName))){
			throw new IllegalArgumentException("Attempt to retrieve index of unknown column.");
		}
		return (nameIndexMapping.get(columnName));
	}


	@Override
	public Object[] getCurrentRecord() {
		return currentRecord;
	}


	@Override
	public void resetToFirstRecord() {
		initializeIterators();
	}


	@Override
	public String getColumnId(int index) {
		return columnDescriptors.get(index-1).getKey();
	}

	@Override
	public Object getValueByColumnID(String columnId) {
		int index = nameIndexMapping.get(columnId);
		return getValueByColumnIndex(index);
	}


	@Override
	public Map<String, Integer> getNameIndexMapping() {
		return nameIndexMapping;
	}
	
	
	public int getRecordCount() {
		return recordCount;
	}

	
}