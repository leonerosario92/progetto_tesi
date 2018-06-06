package dataset;

import java.util.BitSet;
import java.util.Iterator;
import java.util.stream.Stream;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataSet {
	
//	public Iterator<?> getColumnIterator(FieldDescriptor field);
//	
//	public Stream<?> getColumnStream(FieldDescriptor field);
	
	public IColumn<?> getColumn();
	
	public boolean containsColumn (FieldDescriptor field);
	
	public IDataSet getVerticalpartition(FieldDescriptor...field);

	public IRecordIterator getRecordIterator();
	
	public void updateValidityBitset(BitSet validityBits);
	
	public BitSet getValidityBitSet ();
	
}
