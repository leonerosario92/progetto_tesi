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
import datasource.IRecordScanner;
import model.FieldDescriptor;

public class MaterializedDataSet implements IDataSet {
	
	private BitSet validityBitset;
	private int columnCount;
	private int recordCount;
	private List<Object[]> recordList;
	private List<ColumnDescriptor> columnDescriptors;
	private Map<String,Integer> nameIndexMapping;
	
	
	public MaterializedDataSet(List<ColumnDescriptor> columns) {
		this.validityBitset = new BitSet();
		this.columnCount = columns.size();
		this.recordList = new ArrayList<>();
		initializeColumnDescriptors(columns);
	}

	
	private void initializeColumnDescriptors(List<ColumnDescriptor> columns) {
		this.nameIndexMapping = new HashMap<>();
		this.columnDescriptors = new ArrayList<>();
		int index = 0;
		for(ColumnDescriptor descriptor : columns) {
			columnDescriptors.add(descriptor);
			nameIndexMapping.put(descriptor.getKey(), index);
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
		return nameIndexMapping.containsKey(field.getKey());
	}
		
	
	private IColumn<?> buildColumn (int columnIndex){
		ColumnDescriptor descriptor = columnDescriptors.get(columnIndex);
		BaseColumn<?> result = 
				BaseColumnFactory.createColumn(descriptor,recordCount);
		int index = 0;
		for(Object[] record : recordList) {
			result.storeValueAt(record[columnIndex], index);
		}
		return result;
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
		return nameIndexMapping.get(field.getKey());
	}

	@Override
	public Map<String, Integer> getNameIndexMapping() {
		return nameIndexMapping;
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
