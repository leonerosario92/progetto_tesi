package dataset;

import java.util.stream.Stream;

import datasource.IRecordScanner;
import model.FieldDescriptor;

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
	public IRecordMapper getRecordMapper();
	
}
