package impl.base;

import java.util.Iterator;

public class BaseColumnIterator<T> implements Iterator<T> {
	
	private int size;
	private BaseColumn<T> sourceColumn;
	private int iterationIndex;

	public BaseColumnIterator(BaseColumn<T> column) {
		this.size = column.getLength();
		sourceColumn = column;
		iterationIndex = -1;
	}
	
	@Override
	public boolean hasNext() {
		return (iterationIndex < size-1);
	}

	@Override
	public T next() {
		iterationIndex ++;
		return sourceColumn.getValueAt(iterationIndex);
	}

}
