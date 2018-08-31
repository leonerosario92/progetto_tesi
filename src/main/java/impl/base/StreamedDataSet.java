package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IRecordScanner;
import model.FieldDescriptor;

public class StreamedDataSet implements IDataSet{

	private int recordCount;
	private int columnCount;
	private List<ColumnDescriptor> columnDescriptors;
	private Map<String,Integer> nameIndexMapping;
	private Stream<Object[]> recordStream;
	
	
	public StreamedDataSet(Stream<Object[]> recordStream, List<ColumnDescriptor> columnDescriptors, int recordCount) {
		this.recordStream = recordStream;
		this.recordCount = recordCount;
		this.columnCount = columnDescriptors.size();
		initializeColumnDescriptors(columnDescriptors);
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
	

	@Override
	public boolean containsColumn(FieldDescriptor field) {
		return nameIndexMapping.containsKey(field.getKey());
	}

	@Override
	public Stream<Object[]> getRecordStream() {
		return recordStream;
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
	public ColumnDescriptor getColumnDescriptor(int index){
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


	
	
	
	
	
	
	
	
	@Override
	public IColumn<?> getColumn(FieldDescriptor column) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<IColumn<?>> getAllColumns() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IRecordIterator getRecordIterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IRecordScanner getRecordScanner() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void updateValidityBitset(BitSet validityBits) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public BitSet getValidityBitSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
