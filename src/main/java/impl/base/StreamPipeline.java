package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import dataset.ColumnDescriptor;
import dataset.IRecordMapper;
import model.FieldDescriptor;

public class StreamPipeline {

	private int columnCount;
	private List<ColumnDescriptor> columnDescriptors;
	private IRecordMapper recordMapper;
	private Stream<Object[]> recordStream;
	
	
	public StreamPipeline(Stream<Object[]> recordStream, List<ColumnDescriptor> columnDescriptors) {
		this.recordStream = recordStream;
		initializeColumnDescriptors(columnDescriptors);
	}
	
	
	private void initializeColumnDescriptors(List<ColumnDescriptor> columns) {
		this.columnCount = columns.size();
		this.columnDescriptors = new ArrayList<>();
		for(ColumnDescriptor descriptor : columns) {
			columnDescriptors.add(descriptor);
		}
		this.recordMapper = new RecordMapper(columnDescriptors);
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
		return recordMapper.containsKey(field.getKey());
	}

	public Stream<Object[]> getRecordStream() {
		return recordStream;
	}
	
	public int getFieldsCount() {
		return columnCount;
	}

	public ColumnDescriptor getColumnDescriptor(int index){
		return columnDescriptors.get(index);
	}

	public int getColumnIndex(FieldDescriptor field) {
		return recordMapper.getIndex(field);
	}

	public IRecordMapper getRecordMapper () {
		return recordMapper;
	}

}