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
import dataset.IRecordMapper;
import datasource.IRecordScanner;
import model.FieldDescriptor;

public class StreamedDataSet implements IDataSet{
	private int columnCount;
	private List<ColumnDescriptor> columnDescriptors;
	private Iterator<Object[]> sourceIterator;
	private IRecordMapper recordMapper;
	
	
	public StreamedDataSet(List<ColumnDescriptor> columns,Iterator<Object[]> sourceIterator) {
		this.columnCount = columns.size();
		initializeColumnDescriptors(columns);
		this.recordMapper = new RecordMapper(columnDescriptors);
		this.sourceIterator = sourceIterator;
	}

	
	private void initializeColumnDescriptors(List<ColumnDescriptor> columns) {
		this.columnDescriptors = new ArrayList<>();
		for(ColumnDescriptor descriptor : columns) {
			columnDescriptors.add(descriptor);
		}
	}

	
	@Override
	public IRecordIterator getRecordIterator() {
		return new MaterializedRecordIterator(sourceIterator);
	}
	
	
	@Override
	public IRecordScanner getRecordScanner() {
		return new StreamedRecordScanner(this);
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
		return recordMapper.getIndex(field.getKey());
	}

	
	@Override
	public boolean containsColumn(FieldDescriptor field) {
		return recordMapper.containsKey(field.getKey());
	}
	
	
	@Override
	public IRecordMapper getRecordMapper() {
		return this.recordMapper;
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
