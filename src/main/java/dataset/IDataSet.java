package dataset;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface IDataSet {
	
	public boolean containsTable (TableDescriptor table);

	public ITableEntity getTable(TableDescriptor table);
	
	
	public boolean containsColumn (FieldDescriptor field);
	
	public IColumnEntity getColumn(FieldDescriptor field);
	
}
