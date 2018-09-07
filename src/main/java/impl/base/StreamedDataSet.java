package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
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

public class StreamedDataSet implements IDataSet{
	private BitSet validityBitset;
	private int columnCount;
	private List<ColumnDescriptor> columnDescriptors;
	private Map<String,Integer> nameIndexMapping;
	private Iterator<Object[]> sourceIterator;
	
	
	public StreamedDataSet(List<ColumnDescriptor> columns,Iterator<Object[]> sourceIterator) {
		this.validityBitset = new BitSet();
		this.columnCount = columns.size();
		initializeColumnDescriptors(columns);
		initializeValidityBitSet();
		
		this.sourceIterator = sourceIterator;
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

	
	private void initializeValidityBitSet() { }
	
	
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
		return null;
	}


	@Override
	public IRecordIterator getRecordIterator() {
		return new MaterializedRecordIterator(sourceIterator);
	}
	
	
	@Override
	public IRecordScanner getRecordScanner() {
		return new MaterializedRecordScanner(this);
	}

	
	@Override
	public int getRecordCount() {
		return 0;
	}

	
	@Override
	public int getFieldsCount() {
		return columnCount;
	}

	
	@Override
	public void updateValidityBitset(BitSet validityBits) {
		if(validityBits.size() != this.validityBitset.size()) {
			throw new IllegalArgumentException("Operation on validity bits must operate on sets with same size.");
		}
		synchronized (validityBitset) {
			this.validityBitset.and(validityBits);
		}
	}
	
	@Override
	public BitSet getValidityBitSet() {
		return validityBitset;
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

	
	@Override
	public Stream<Object[]> getRecordStream() {
		Stream<Object[]> recordStream = StreamSupport.stream
				(
					Spliterators.spliterator(getRecordIterator(),0, 0)
					,false
				);
		return recordStream;
	}

}
