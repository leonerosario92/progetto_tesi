package dataset;

import model.IDescriptor;

public interface IRecordMapper {
	
	public int getIndex(IDescriptor descriptor);
	
	public int getIndex(String fieldKey);
	
	public String getFieldKey(int fieldIndex);

	public Boolean containsKey(String key);
	
}
