package dataset;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface ILayoutManager {
	
	public ITableEntity  loadTable(TableDescriptor table);
	
	public IColumnEntity  loadColumn(FieldDescriptor column);
	
}
