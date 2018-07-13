package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.stream.Stream;

import dataset.ColumnDescriptor;
import dataset.IColumn;

/*==== IColumn implementation ====*/
public class BaseColumn<T> implements IColumn<T>{
	
	private Object[] values;
	private ColumnDescriptor descriptor;
	private int length;
	
	public BaseColumn(ColumnDescriptor descriptor,int length) {
		this.descriptor = descriptor;
		this.length = length;
		this.values = new Object[length];
	}
	
	public BaseColumn(ColumnDescriptor descriptor, ArrayList<T> values) {
		this.descriptor = descriptor;
		this.length = values.size();
		this.values = values.toArray(new Object[length]);
	}

	@Override
	public Iterator<T> getColumnIterator() {
		return new BaseColumnIterator<T>(this);
	}

	@Override
	public T getValueAt(int index) {
		return (T)values[index];
	}

	@Override
	public void storeValueAt(Object value, int index) {
		values[index] = value;
	}
	
	@Override
	public ColumnDescriptor getDescriptor() {
		return descriptor;
	}
	
	@Override
	public int getLength() {
		return this.length;
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
