package dataset;

import model.FieldDescriptor;
import model.TableDescriptor;

public interface ILayoutManager {
	
	public IDataSet loadTable(TableDescriptor table, IRecordIterator tableIterator);
	
	public IDataSet loadColumn (FieldDescriptor field, IColumnIterator columnIterator);
}
