package dataset;

import java.util.Iterator;
import java.util.stream.Stream;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataSet {
	
//	public boolean containsTable (TableDescriptor table);
//
//	public ITableEntity getTable(TableDescriptor table);
	
	public Iterator<?> getColumnIterator(FieldDescriptor field);
	
	public Stream<?> getColumnStream(FieldDescriptor field);
	
	public boolean containsColumn (FieldDescriptor field);
	
	public IDataSet getSubset(FieldDescriptor...field);

	public IRecordIterator tableIterator();
	
}
