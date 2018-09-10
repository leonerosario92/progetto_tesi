package impl.base;

import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dataset.ColumnDescriptor;
import dataset.IRecordMapper;
import model.IDescriptor;

public class RecordMapper implements IRecordMapper{
	
	private BiMap<String,Integer> mapper;
	
	
	public RecordMapper(List<ColumnDescriptor> columns) {
		this.mapper = HashBiMap.create();
		int index = 0;
		for(ColumnDescriptor descriptor : columns) {
			mapper.put(descriptor.getKey(), index);
			index ++;
		}
	}
	
	
	public RecordMapper(Map<String,Integer> nameIndexMapping) {
		this.mapper = HashBiMap.create(nameIndexMapping);
	}

	
	@Override
	public int getIndex(IDescriptor descriptor) {
		return mapper.get(descriptor.getKey());
	}
	
	
	@Override
	public int getIndex(String fieldKey) {
		return mapper.get(fieldKey);
	}

	
	@Override
	public String getFieldKey(int fieldIndex) {
		return mapper.inverse().get(fieldIndex);
	}


	@Override
	public Boolean containsKey(String key) {
		return mapper.containsKey(key);
	}

	
	



}
