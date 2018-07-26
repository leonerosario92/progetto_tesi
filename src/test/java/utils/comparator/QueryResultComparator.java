package utils.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import dataset.IRecordIterator;
import datasource.IRecordScanner;
import datatype.DataType;
import datatype.TypeComparator;

public class QueryResultComparator  {
	
	private QueryResultComparator() {}


	public static boolean compareValues(IRecordScanner scanner0, IRecordScanner scanner1) {
		 
		boolean mdResult = compareMetaData(scanner0, scanner1);
		if(! mdResult) {
			return false;
		}
		
		int fieldsCount = scanner0.getFieldsCount();
		for(int index=1; index<=fieldsCount; index++) {
			
			TypeComparator fieldComparator = scanner0.getColumnType(index).getComparator();
			
			ArrayList<Object> col0 = new ArrayList<>();
			ArrayList<Object> col1 = new ArrayList<>();

			scanner0.resetToFirstRecord();
			scanner1.resetToFirstRecord();
			
			String columnName = scanner0.getColumnId(index);
			
			while(scanner0.next()) {
				col0.add(scanner0.getValueByColumnIndex(index));
			}
			
			while(scanner1.next()) {
				col1.add(scanner1.getValueByColumnID(columnName));
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
	
	
	private static boolean compareMetaData(IRecordScanner iterator0, IRecordScanner iterator1) {

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
			String columnName = iterator1.getColumnName(i);
			if(! (fields.contains(columnName))) {
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