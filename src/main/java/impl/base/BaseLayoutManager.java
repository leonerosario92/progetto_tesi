package impl.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import context.DataType;
import dataset.AbstractLayoutManager;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import model.FieldDescriptor;

public class BaseLayoutManager extends AbstractLayoutManager {

	/*======INNER CLASSES THAT IMPLEMENTS DATASET=====*/
	private class ColumnImpl<T>{
		
		private ArrayList<T> column;
		
		public void storeValueAt(int index, Object val) {
			T value = (T) val;
			column.set(index,value);
		}
		
		public Iterator<T> getIterator(){
			return column.iterator();
		}
		
		public Stream<T> getStream(){
			return column.stream();
		}
		
	}
	
	
	private class BaseDataSet implements IDataSet {
		
		private HashMap<String, ColumnImpl<?>> columns;
		
		public BaseDataSet(){
			columns = new HashMap<>();
		}


		@Override
		public Iterator<?> getColumnIterator(FieldDescriptor field) {
			String key = getFieldKey(field);
			if(!(columns.containsKey(key))) {
				//TODO Manage exception properly
				throw new IllegalArgumentException();
			}
			return columns.get(key).getIterator();
		}

		@Override
		public Stream<?> getColumnStream(FieldDescriptor field) {
			String key = getFieldKey(field);
			if(!(columns.containsKey(key))) {
				//TODO Manage exception properly
				throw new IllegalArgumentException();
			}
			return columns.get(key).getStream();
		}

		@Override
		public boolean containsColumn(FieldDescriptor field) {
			String key = getFieldKey(field);
			return columns.containsKey(key);
		}

		@Override
		public IDataSet getSubset(FieldDescriptor... field) {
		
		}
		
		private String getFieldKey (FieldDescriptor field) {
			return getFieldKey(field.getTable().getName(), field.getName());
		}
		
		private String getFieldKey(String tableName, String columnName) {
			return tableName + "." + columnName;
		}

		protected void addColumn(String tableName, String columnName, ColumnImpl<?> newColumn) {
			String key = getFieldKey(tableName, columnName);
			columns.put(key, newColumn);
		}

		@Override
		public IRecordIterator tableIterator() {
			
		}
	}
	
	
	
	public BaseLayoutManager() {
		super();
	}
	
	
	/*======METHODS INHERITED FROM ILayoutManager=====*/
	@Override
	public IDataSet buildDataSet(IRecordIterator iterator) {
		
		BaseDataSet dataSet = new BaseDataSet();
		
		int fieldsCount = iterator.getFieldsCount();
		ColumnImpl<?>[] columns = new ColumnImpl<?>[fieldsCount];
		for(int index = 1; index <= fieldsCount; index++) {
			DataType fieldType = iterator.getColumnType(index);
			columns[index] = createColumn (fieldType);
		}
		
		Object value;
		int columnIndex = 0;
		while(iterator.hasNext()) {
			for(int index = 1; index <= fieldsCount; index++) {
				value = iterator.getValueAt(index);
				columns[index].storeValueAt(columnIndex, value);
			}
		}
		
		for(int index = 1; index <= fieldsCount; index++) {
			String columnName = iterator.getColumnName(index);
			String tableName = iterator.getTableName(index);
			dataSet.addColumn(tableName, columnName, columns[index]);
		}
		
		return dataSet;
	}

	
	@Override
	public IDataSet mergeDatasets(Set<IDataSet> partialResults) {
		
	}

	
	private ColumnImpl<?> createColumn(DataType fieldsType) {
		ColumnImpl<?> newColumn = null;
		switch (fieldsType) {
		case INTEGER:
			newColumn = new ColumnImpl<Integer>();
			break;
		case DOUBLE:
			newColumn = new ColumnImpl<Double>();
			break;
		case FLOAT:
			newColumn = new ColumnImpl<Float>();
			break;
		case LONG:
			newColumn = new ColumnImpl<Long>();
			break;
		case STRING:
			newColumn = new ColumnImpl<String>();
			break;
		case BIG_DECIMAL:
			newColumn = new ColumnImpl<BigDecimal>();
			break;
		default:
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
		return newColumn;
	}

}
