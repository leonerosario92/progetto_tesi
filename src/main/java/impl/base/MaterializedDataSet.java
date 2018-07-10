package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IRecordScanner;
import datatype.DataType;
import model.FieldDescriptor;

public class MaterializedDataSet implements IDataSet {
	
	private BitSet validityBitset;
	private int columnCount;
	private int recordCount;
	private List<Object[]> recordList;
	private List<ColumnDescriptor> columnDescriptors;
	private Map<String,Integer> nameIndexMapping;
	
	public MaterializedDataSet(int recordCount,List<FieldDescriptor> columns) {
		this.validityBitset = new BitSet();
		this.columnCount = columns.size();
		this.recordCount = recordCount;
		this.recordList = new ArrayList<>(recordCount);
		initializeColumnDescriptors(columns);
		initializeValidityBitSet();
	}

	private void initializeColumnDescriptors(List<FieldDescriptor> columns) {
		this.nameIndexMapping = new HashMap<>();
		this.columnDescriptors = new ArrayList<>();
		int index = 0;
		for(FieldDescriptor field : columns) {
			ColumnDescriptor newColumn = new ColumnDescriptor(
					field.getTable().getName(),
					field.getName(), 
					field.getType()
			);
			columnDescriptors.add(newColumn);
			nameIndexMapping.put(field.getKey(), index);
			index ++;
		}
		
	}

	private void initializeValidityBitSet() {
    	this.validityBitset = new BitSet(recordCount);
    	validityBitset.set(0,recordCount,true);
    }
	
	
	public void addRecord(Object[] record) {
		if(record.length != columnCount) {
			throw new IllegalArgumentException("Attempt to add record with invalid number of fields");
		}
		recordList.add(record);
	}
	
	
	public void addRecords(Iterable<Object[]> records) {
		for(Object[] record : records) {
			addRecord(record);
		}
	}
	
	
	@Override
	public IColumn<?> getColumn(FieldDescriptor column) {
		int columnIndex = nameIndexMapping.get(column.getKey());
		return buildColumn(columnIndex);
	}

	
	@Override
	public boolean containsColumn(FieldDescriptor field) {
		return nameIndexMapping.containsKey(field.getKey());
	}
	
	
	@Override
	public List<IColumn<?>> getAllColumns() {
		List<IColumn<?>> result = new ArrayList<>();
		for(int i=0; i<columnCount; i++) {
			IColumn<?> currentColumn = buildColumn(i);
			result.add(currentColumn);
		}
		return result;
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
		Iterator<Object[]> recordIterator = recordList.iterator();
		return new IRecordIterator() {
			
			@Override
			public Object[] next() {
				return recordIterator.next();
			}
			
			@Override
			public boolean hasNext() {
				return recordIterator.hasNext();			}
		};
	}
	

	@Override
	public IRecordScanner getRecordScanner() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int getRecordCount() {
		return recordCount;
	}

	
	@Override
	public int getFieldsCount() {
		return columnCount;
	}

	
	@Override
	public void updateValidityBitset(BitSet validityBits) {
		if(validityBits.size() != this.validityBitset.size()) {
			throw new IllegalArgumentException("Operation on validity bits must operate on sets with same size");
		}
		synchronized (validityBitset) {
			this.validityBitset.and(validityBits);
		}
	}

	
	@Override
	public BitSet getValidityBitSet() {
		return validityBitset;
	}

	
	public List<Object[]> getRecordList() {
		return recordList;
	}

	@Override
	public ColumnDescriptor getColumnDescriptor(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnIndex(FieldDescriptor field) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Integer> getNameIndexMapping() {
		return nameIndexMapping;
	}

}
