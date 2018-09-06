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
import query.execution.operator.StreamProcessingOperator;

public class StreamPipeline {

	private int recordCount;
	private int columnCount;
	private List<ColumnDescriptor> columnDescriptors;
	private Map<String,Integer> nameIndexMapping;
	private Stream<Object[]> recordStream;
	
	
	public StreamPipeline(Stream<Object[]> recordStream, List<ColumnDescriptor> columnDescriptors, int recordCount) {
		this.recordStream = recordStream;
		this.recordCount = recordCount;
		initializeColumnDescriptors(columnDescriptors);
	}
	
	
	private void initializeColumnDescriptors(List<ColumnDescriptor> columns) {
		this.columnCount = columns.size();
		this.nameIndexMapping = new HashMap<>();
		this.columnDescriptors = new ArrayList<>();
		int index = 0;
		for(ColumnDescriptor descriptor : columns) {
			columnDescriptors.add(descriptor);
			nameIndexMapping.put(descriptor.getKey(), index);
			index ++;
		}
	}
	
	
	public void updateStream (Stream<Object[]> stream) {
		synchronized(this) {
			this.recordStream = stream;
		}
	}
	
	
	public void updateColumnDescriptors (List<ColumnDescriptor> newColumns) {
		synchronized(this) {
			initializeColumnDescriptors(newColumns);
		}
	}
	

	public boolean containsColumn(FieldDescriptor field) {
		return nameIndexMapping.containsKey(field.getKey());
	}

	public Stream<Object[]> getRecordStream() {
		return recordStream;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public int getFieldsCount() {
		return columnCount;
	}

	public ColumnDescriptor getColumnDescriptor(int index){
		return columnDescriptors.get(index);
	}

	public int getColumnIndex(FieldDescriptor field) {
		return nameIndexMapping.get(field.getKey());
	}

	public Map<String, Integer> getNameIndexMapping() {
		return nameIndexMapping;
	}

}