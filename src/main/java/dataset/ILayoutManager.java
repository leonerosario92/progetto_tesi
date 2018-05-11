package dataset;

import model.ITableDescriptor;

public interface ILayoutManager {
	
	public TableEntity  loadTable(ITableDescriptor table);
	
	public ColumnEntity  loadColumn(IColumnDescriptor column);
	
}
