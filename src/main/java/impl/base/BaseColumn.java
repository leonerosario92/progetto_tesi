package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.stream.Stream;

import dataset.ColumnDescriptor;
import dataset.IColumn;

/*==== IColumn implementation ====*/
public class BaseColumn<T> implements IColumn<T>{
	
	private ArrayList<T> values;
	private ColumnDescriptor descriptor;
	private int length;
	
	public BaseColumn(ColumnDescriptor descriptor,int length) {
		this.descriptor = descriptor;
		this.length = length;
		this.values = new ArrayList<>(length);
	}
	
	public BaseColumn(ColumnDescriptor descriptor, ArrayList<T> values) {
		this.descriptor = descriptor;
		this.length = values.size();
		this.values = values;
	}

	@Override
	public Iterator<T> getColumnIterator() {
		return values.iterator();
	}

	@Override
	public Stream<T> getColumnStream() {
		return values.stream();
	}

	@Override
	public T getValueAt(int index) {
		return values.get(index);
	}

	@Override
	public void storeValueAt(Object value, int index) {
		T typedValue = (T) value;
		values.add(index,typedValue);
	}
	
	@Override
	public ColumnDescriptor getDescriptor() {
		return descriptor;
	}
	
	public BaseColumn<T> getFilteredInstance (BitSet bitSet) {
		int newLength = bitSet.cardinality();
		
		ArrayList<T> filteredValues = new ArrayList<>(newLength);
		 for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
			 filteredValues.add(getValueAt(i));
		 }
		 return new BaseColumn<T>(descriptor,filteredValues);
	}
	
}
