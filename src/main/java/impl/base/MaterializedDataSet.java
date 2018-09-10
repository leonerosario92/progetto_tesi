package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import dataset.IRecordMapper;
import datasource.IRecordScanner;
import model.FieldDescriptor;

public class MaterializedDataSet implements IDataSet {
	
	private int columnCount;
	private int recordCount;
	private List<Object[]> recordList;
	private List<ColumnDescriptor> columnDescriptors;
	private RecordMapper recordMapper;
	
	public MaterializedDataSet(List<ColumnDescriptor> columns) {
		this.columnCount = columns.size();
		this.recordList = new ArrayList<>();
		this.recordMapper = new RecordMapper(columns);
		initializeColumnDescriptors(columns);
	}

	
	private void initializeColumnDescriptors(List<ColumnDescriptor> columns) {
		this.columnDescriptors = new ArrayList<>();
		int index = 0;
		for(ColumnDescriptor descriptor : columns) {
			columnDescriptors.add(descriptor);
			index ++;
		}
	}

	
	public void addRecord(Object[] record) {
		if(record.length != columnCount) {
			throw new IllegalArgumentException("Attempt to add record with invalid number of fields.");
		}
		recordList.add(record);
	}
	
	
	public void addRecords(Iterable<Object[]> records) {
		for(Object[] record : records) {
			addRecord(record);
		}
	}
	
	
	@Override
	public boolean containsColumn(FieldDescriptor field) {
		return recordMapper.containsKey(field.getKey());
	}
		
	
	@Override
	public IRecordIterator getRecordIterator() {
			return new MaterializedRecordIterator(this);
	}
	
	
	@Override
	public IRecordScanner getRecordScanner() {
		return new MaterializedRecordScanner(this);
	}
	
	
	public int getRecordCount() {
		return recordCount;
	}

	
	@Override
	public int getFieldsCount() {
		return columnCount;
	}


	@Override
	public ColumnDescriptor getColumnDescriptor(int index) {
		return columnDescriptors.get(index);
	}

	@Override
	public int getColumnIndex(FieldDescriptor field) {
		return recordMapper.getIndex(field);
	}

	
//	@Override
//	public Map<String, Integer> getRecordMapper() {
//		return nameIndexMapping;
//	}
	@Override
	public IRecordMapper getRecordMapper() {
		return recordMapper;
	}

	
	public List<Object[]> getRecordList() {
		return recordList;
	}

	
	@Override
	public Stream<Object[]> getRecordStream() {
		Stream<Object[]> recordStream = StreamSupport.stream
				(
					Spliterators.spliterator(getRecordIterator(),recordCount, 0)
					,false
				);
		return recordStream;
	}
}
