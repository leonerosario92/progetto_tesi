package utils.comparator;


import dataset.IRecordIterator;
import datatype.DataType;

public class RecordIteratorComparator  {

	public boolean compareMetaData(IRecordIterator iterator0, IRecordIterator iterator1) {
		int rc0 = iterator0.getRecordCount();
		int rc1 = iterator1.getRecordCount();
		
		if(rc0 != rc1) {
			return false;
		}
		
		int fc0 = iterator0.getFieldsCount();
		int fc1 = iterator0.getFieldsCount();
		
		if(fc0 != fc1) {
			return false;
		}
		
		String name0, name1;
		DataType type0, type1;
		for(int i=0; i<fc0; i++) {
			name0 = iterator0.getColumnName(i);
			name1 = iterator1.getColumnName(i);
			if  (! (name0.equals(name1))) {
				return false;
			}
			type0 = iterator0.getColumnType(i);
			type1 = iterator1.getColumnType(i);
			if(! (type0.equals(type1))) {
				return false;
			}
		}
		return true;
	}


}
