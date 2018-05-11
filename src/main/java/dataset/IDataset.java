package dataset;

import model.ITableDescriptor;

public interface IDataset {
	
	public ITableEntity getTable (ITableDescriptor table);
	
	public IColumnEntity getColumn (IColumnDescriptor column);
	
	public IRecordEntity getRecord (IRecordIterator record);
	
}
