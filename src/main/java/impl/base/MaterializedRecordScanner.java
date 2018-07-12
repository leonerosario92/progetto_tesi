package impl.base;

import java.util.Iterator;

import datasource.IRecordScanner;
import datatype.DataType;
import model.FieldDescriptor;

public class MaterializedRecordScanner implements IRecordScanner{
	
	private Iterator<Object[]> recordIterator;
	private Object[] currentRecord;
	private MaterializedDataSet sourceDataSet;
	
	public MaterializedRecordScanner(MaterializedDataSet dataset) {
		this.sourceDataSet = dataset;
		this.recordIterator = dataset.getRecordIterator();
		currentRecord = null;
	}

	@Override
	public boolean next() {
		if(recordIterator.hasNext()) {
			currentRecord = recordIterator.next();
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void resetToFirstRecord() {
		recordIterator = sourceDataSet.getRecordIterator();
	}

	@Override
	public DataType getColumnType(int index) {
		return sourceDataSet.getColumnDescriptor(index-1).getColumnType();
	}

	@Override
	public String getColumnName(int index) {
		return sourceDataSet.getColumnDescriptor(index-1).getColumnName();
	}

	@Override
	public String getTableName(int index) {
		return sourceDataSet.getColumnDescriptor(index-1).getTableName();
	}

	@Override
	public int getColumnIndex(FieldDescriptor field) {
		return (sourceDataSet.getColumnIndex(field) + 1);
	}

	@Override
	public Object[] getCurrentRecord() {
		return currentRecord;
	}

	@Override
	public Object getValueByColumnIndex(int index) {
		return currentRecord[index-1];
	}

	@Override
	public Object getValueByColumnDescriptor(FieldDescriptor field) {
		int index = getColumnIndex(field);
		return getValueByColumnIndex(index -1);
	}

	@Override
	public int getFieldsCount() {
		return sourceDataSet.getFieldsCount();
	}

	@Override
	public int getRecordCount() {
		return sourceDataSet.getRecordCount();
	}

	@Override
	public String getColumnId(int index) {
		return sourceDataSet.getColumnDescriptor(index -1).getKey();
	}

	@Override
	public Object getValueByColumnID(String columnId) {
		int index = sourceDataSet.getNameIndexMapping().get(columnId);
		return getValueByColumnIndex(index);
	}
	
}