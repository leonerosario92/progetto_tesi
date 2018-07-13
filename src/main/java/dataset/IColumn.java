package dataset;

import java.util.Iterator;
import java.util.stream.Stream;

import model.FieldDescriptor;

public interface IColumn<T> {

	public Iterator<T> getColumnIterator();
	
//	public Stream<T> getColumnStream();
	
	public T getValueAt(int index);
	
	public void storeValueAt(T value, int index);
	
	public ColumnDescriptor getDescriptor();
	
	public int getLength();
}
