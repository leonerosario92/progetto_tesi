package impl.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import context.DataType;
import dataset.LayoutManager;
import dataset.IDataSet;
import dataset.IRecordIterator;
import model.FieldDescriptor;

public class BaseLayoutManager extends LayoutManager {
	

	/*======INNER CLASSES THAT IMPLEMENTS DATASET=====*/
	private class ColumnImpl<T>{
		
		private ArrayList<T> column;
		
		public ColumnImpl() {
			this.column = new ArrayList<>();
		}
		
		public void storeValueAt(int index, Object val) {
			T value = (T) val;
			column.add(index,value);
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
		private BitSet validityBitset;
		private int recordCount;
		
		public BaseDataSet(int recordCount){
			this.recordCount = recordCount;
			this.columns = new HashMap<>();
			initializeValidityBitSet(recordCount);
		}


		@Override
		public Iterator<?> getColumnIterator(FieldDescriptor field) {
			String key = getFieldKey(field);
			if(!(columns.containsKey(key))) {
				throw new IllegalArgumentException("Attempt to refer a non-existent column");
			}	
			return columns.get(key).getIterator();
		}

		
		@Override
		public Stream<?> getColumnStream(FieldDescriptor field) {
			String key = getFieldKey(field);
			if(!(columns.containsKey(key))) {
				throw new IllegalArgumentException("Attempt to refer a non-existent column");
			}
			return columns.get(key).getStream();
		}

		
		@Override
		public boolean containsColumn(FieldDescriptor field) {
			String key = getFieldKey(field);
			return columns.containsKey(key);
		}

		
		//TODO
		@Override
		public IDataSet getVerticalpartition(FieldDescriptor... fields) {
			return null;
		}
		
		
		private String getFieldKey (FieldDescriptor field) {
			return getFieldKey(field.getTable().getName(), field.getName());
		}
		
		
		private String getFieldKey(String tableName, String columnName) {
			return tableName + "." + columnName;
		}

		
	    void addColumn(String tableName, String columnName, ColumnImpl<?> newColumn) {
			String key = getFieldKey(tableName, columnName);
			columns.put(key, newColumn);
		}
	    
	    private void initializeValidityBitSet(int recordNum) {
	    	this.validityBitset = new BitSet(recordNum);
	    	validityBitset.set(0,recordNum,true);
	    }

		//TODO
		@Override
		public IRecordIterator getRecordIterator() {
			return null;
		}

		
		@Override
		public void updateValidityBitset(BitSet validityBits) {
			if(validityBits.size() != this.validityBitset.size()) {
				throw new IllegalArgumentException("Operation on validity bits must operate on sets with same size");
			}
			synchronized (validityBitset) {
				this.validityBitset.and(validityBits);
			}
		}


		@Override
		public BitSet getValidityBitSet() {
			BitSet bitSet;
			synchronized (validityBitset) {
				bitSet = (BitSet) validityBitset.clone();
			}
			return bitSet;
		}
		
	}
	
	
	public BaseLayoutManager() {
		super();
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
			throw new IllegalArgumentException();
		}
		return newColumn;
	}
	
	
	/*======METHODS INHERITED FROM ILayoutManager=====*/
	@Override
	public IDataSet buildDataSet(IRecordIterator iterator) {
		
		BaseDataSet dataSet = new BaseDataSet(iterator.getRecordCount());
		
		/*
		 * For each field in iteraror metadata, create a new column
		*/
		int fieldsCount = iterator.getFieldsCount();
		ColumnImpl<?>[] columns = new ColumnImpl<?>[fieldsCount+1];
		for(int index = 1; index <= fieldsCount; index++) {
			DataType fieldType = iterator.getColumnType(index);
			columns[index] = createColumn (fieldType);
		}
		
		/*
		 * For each position, fill each column with corresponding value
		 */
		Object value;
		int columnIndex = 0;
		while(iterator.hasNext()) {
			iterator.next();
			for(int index = 1; index <= fieldsCount; index++) {
				value = iterator.getValueByIndex(index);
				columns[index].storeValueAt(columnIndex, value);
			}
		}
		
		/*
		 * Add every column to the new dataset
		 */
		for(int index = 1; index <= fieldsCount; index++) {
			String columnName = iterator.getColumnName(index);
			String tableName = iterator.getTableName(index);
			dataSet.addColumn(tableName, columnName, columns[index]);
		}
		return dataSet;
	}


	@Override
	public IDataSet mergeDatasets(Set<IDataSet> dataSets) {
		
		BitSet mergedBitSet = mergeValidityBitsets(dataSets);
		IDataSet mergedDataSet = buildDataSet(dataSets,mergedBitSet);
	}


	private BitSet mergeValidityBitsets(Set<IDataSet> dataSets) {
		BitSet mergedValidityBitSet = null;
		Iterator<IDataSet> it = dataSets.iterator();
		if(it.hasNext()) {
			 mergedValidityBitSet  = it.next().getValidityBitSet();
		}
		while(it.hasNext()) {
			BitSet bitSet = it.next().getValidityBitSet();
			if(mergedValidityBitSet.size() != bitSet.size()) {
				throw new IllegalArgumentException("Only dataset with same size can be merged on validity bitSet");
			}
			mergedValidityBitSet.and(bitSet);
		}
		return mergedValidityBitSet;
	}


	private IDataSet buildDataSet(Set<IDataSet> dataSets, BitSet mergedBitSet) {
		int columnSize = mergedBitSet.cardinality();
		IDataSet result = new BaseDataSet(columnSize);
		for(IDataSet dataSet : dataSets) {
			dataSet.getColumnIterator()
		}
	}
}
