package dataset;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import datasource.IRecordScanner;
import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataSet  {
	
//	public IColumn<?> getColumn(FieldDescriptor column);
//	public List<IColumn<?>> getAllColumns();
//	public int getRecordCount();
// 	public IDataSet getVerticalpartition(FieldDescriptor...field);
//	public void updateValidityBitset(BitSet validityBits);
//	public BitSet getValidityBitSet ();
	
	public boolean containsColumn (FieldDescriptor field);

	public IRecordIterator getRecordIterator();
	
	public IRecordScanner getRecordScanner();
	
	public Stream <Object[]> getRecordStream();
	
	public int getFieldsCount();
	
	public ColumnDescriptor getColumnDescriptor(int index);
	
	public int getColumnIndex(FieldDescriptor field);

	/*Substitute that with recordMapper interface */
	public Map<String, Integer> getNameIndexMapping();
	
}
