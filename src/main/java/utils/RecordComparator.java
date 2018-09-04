package utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import datatype.TypeComparator;
import model.FieldDescriptor;

public class RecordComparator {
	
	private RecordComparator() {}
	
	public static Comparator<Object[]> getRecordComparator (
		Map<String,Integer> nameIndexMapping, 
		List<FieldDescriptor> orderingSequence)
	{
		int size = orderingSequence.size();
		TypeComparator[] comparators = new TypeComparator [size];
		int[] indexes = new int[size];
		int fieldIndex=0;
		for(FieldDescriptor field : orderingSequence) {
			indexes[fieldIndex] = nameIndexMapping.get(field.getKey());
			comparators[fieldIndex] = 
					field
					.getType()
					.getComparator();
			fieldIndex ++;
		}
		
		Comparator<Object[]> recordComparator = 
				(record,other)-> comparators[0].compare(record[indexes[0]], other[indexes[0]]);

		for(int i=1; i<size; i++ ) {
			final int currentIndex = i;
			recordComparator = recordComparator.thenComparing(
				new Comparator<Object[]>() {
					@Override
					public int compare(Object[] record, Object[] other) {
						return comparators[currentIndex].
								compare(record[indexes[currentIndex]], other[indexes[currentIndex]]);
					}
				}
			);
			
		}
		
		return recordComparator;
	}

}
