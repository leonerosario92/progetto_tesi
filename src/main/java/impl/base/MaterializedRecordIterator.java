package impl.base;

import java.util.Iterator;
import java.util.List;

import dataset.IRecordIterator;

public class MaterializedRecordIterator implements IRecordIterator {
	
	private List<Object[]> recordList;
	private Iterator<Object[]> recordIterator;
	
	public MaterializedRecordIterator(MaterializedDataSet dataset) {
		this.recordList = dataset.getRecordList();
		this.recordIterator = recordList.iterator();
	}
	
	public  MaterializedRecordIterator(Iterator<Object[]> sourceIterator) {
		this.recordIterator = sourceIterator;
	}

	@Override
	public boolean hasNext() {
		return recordIterator.hasNext();
	}

	@Override
	public Object[] next() {
		return recordIterator.next();
	}
	
}
