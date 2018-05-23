package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecord;
import dataset.IRecordIterator;
import model.FieldDescriptor;

public class BaseLayoutManager implements ILayoutManager {
	
	
	private class BaseDataSet implements IDataSet {
		
		private HashMap<FieldDescriptor, ArrayList<?>> columns;
		
		public BaseDataSet(){
			columns = new HashMap<>();
		}

		@Override
		public Iterator<?> getColumnIterator(FieldDescriptor field) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Stream<?> getColumnStream(FieldDescriptor field) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsColumn(FieldDescriptor field) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public IDataSet getSubset(FieldDescriptor... field) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	@Override
	public IDataSet newDataSet(IRecordIterator it) {
		
		BaseDataSet dataSet = new BaseDataSet();
		int fieldsCount = it.getFieldsCount();
		
		
		IRecord record;
		while(it.hasNext()) {
			record = it.getNextRecord();
			
		}
	}

	
	@Override
	public IDataSet mergeDatasets(Set<IDataSet> partialResults) {
		// TODO Auto-generated method stub
		return null;
	}


}
