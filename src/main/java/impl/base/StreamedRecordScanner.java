package impl.base;

import java.util.Iterator;
import java.util.Map;

import dataset.IRecordMapper;
import datasource.IRecordScanner;
import datatype.DataType;
import model.FieldDescriptor;

public class StreamedRecordScanner implements IRecordScanner {

	private Iterator<Object[]> recordIterator;
	private Object[] currentRecord;
	private StreamedDataSet sourceDataSet;
	private IRecordMapper recordMapper;
	
	
	public StreamedRecordScanner(StreamedDataSet dataset) {
		this.sourceDataSet = dataset;
		this.recordMapper = sourceDataSet.getRecordMapper();
		this.recordIterator = dataset.getRecordIterator();
		this.currentRecord = null;
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
	public String getColumnId(int index) {
		return sourceDataSet.getColumnDescriptor(index -1).getKey();
	}

	
	@Override
	public Object getValueByColumnID(String columnId) {
		int index = recordMapper.getIndex(columnId);
		return getValueByColumnIndex(index);
	}


	@Override
	public IRecordMapper getRecordMapper() {
		return recordMapper;
	}

	
}
