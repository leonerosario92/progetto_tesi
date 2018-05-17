package dataset;

public interface IRecordIterator {
	
	public IRecord getNextRecord();
	
	public boolean hasNext();
	
}
