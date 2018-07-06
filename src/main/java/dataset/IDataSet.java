package dataset;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import datasource.IRecordScanner;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataSet {
	
	public IColumn<?> getColumn(FieldDescriptor column);
	
	public List<IColumn<?>> getAllColumns();
	
	public boolean containsColumn (FieldDescriptor field);
	
	//public IDataSet getVerticalpartition(FieldDescriptor...field);

	public IRecordIterator getRecordIterator();
	
	public IRecordScanner getRecordScanner();
	
	public int getRecordCount();
	
	public int getFieldsCount();
	
	public void updateValidityBitset(BitSet validityBits);
	
	public BitSet getValidityBitSet ();
	
}
