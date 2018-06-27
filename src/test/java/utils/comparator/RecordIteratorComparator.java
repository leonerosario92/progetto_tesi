package utils.comparator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import dataset.IRecordIterator;
import datatype.DataType;
import datatype.TypeComparator;

public class RecordIteratorComparator  {
	
	private RecordIteratorComparator() {}


	public static boolean compareValues(IRecordIterator iterator0, IRecordIterator iterator1) {
		 
		boolean mdResult = compareMetaData(iterator0, iterator1);
		if(! mdResult) {
			return false;
		}
		
		int fieldsCount = iterator0.getFieldsCount();
		for(int i=1; i<=fieldsCount; i++) {
			TypeComparator fieldComparator = iterator0.getColumnType(i).getComparator();
			
			ArrayList<Object> col0 = new ArrayList<>();
			ArrayList<Object> col1 = new ArrayList<>();

			iterator0.resetToFirstRecord();
			iterator1.resetToFirstRecord();
			
			String columnName = iterator0.getColumnName(i);
			
			while(iterator0.hasNext()) {
				iterator0.next();
				col0.add(iterator0.getValueByColumnIndex(i));
			}
			
			while(iterator1.hasNext()) {
				iterator1.next();
				col1.add(iterator1.getValueByColumnName(columnName));
			}
			
			Collections.sort(col0, fieldComparator);
			Collections.sort(col1,fieldComparator);
			
			boolean result;
			if (!( result = compareSingleValues(col0.iterator(),col1.iterator(), fieldComparator))) {
				return false;
			}
		}
		return true;
	}
	
	
	private static boolean compareMetaData(IRecordIterator iterator0, IRecordIterator iterator1) {

		int fc0 = iterator0.getFieldsCount();
		int fc1 = iterator1.getFieldsCount();
		
		if(fc0 != fc1) {
			return false;
		}
		
		Set<String> fields = new HashSet<String>();
		for(int i=1; i<=fc0; i++) {
			fields.add(iterator0.getColumnName(i));
		}
		
		for(int i=1; i<=fc0; i++) {
			if(! (fields.contains(iterator1.getColumnName(i)))) {
				return false;
			}
		}
		return true;
	}


	private static boolean compareSingleValues(Iterator<Object> it1, Iterator<Object> it2,
			Comparator<Object> fieldComparator) {
		while(it1.hasNext()) {
			if(! it2.hasNext()) {
				throw new IllegalStateException("The two iterators under test are supposed to have same number of elements");
			}
			int result = fieldComparator.compare(it1.next(), it2.next());
			if(result != 0) {
				return false;
			}
		}
		return true;
	}

}
