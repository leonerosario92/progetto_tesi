package impl.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import context.DataType;
import dataset.LayoutManager;
import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import model.FieldDescriptor;

public class BaseLayoutManager extends LayoutManager {
	
	
	/*==== IColumn implementation ====*/
	private class ColumnImpl<T> implements IColumn<T>{
		
		private ArrayList<T> values;
		private ColumnDescriptor descriptor;
		private int length;
		
		public ColumnImpl(ColumnDescriptor descriptor,int length) {
			this.descriptor = descriptor;
			this.length = length;
			this.values = new ArrayList<>(length);
		}
		
		public ColumnImpl(ColumnDescriptor descriptor, ArrayList<T> values) {
			this.descriptor = descriptor;
			this.length = values.size();
			this.values = values;
		}

		@Override
		public Iterator<T> getColumnIterator() {
			return values.iterator();
		}

		@Override
		public Stream<T> getColumnStream() {
			return values.stream();
		}

		@Override
		public T getValueAt(int index) {
			return values.get(index);
		}

		@Override
		public void storeValueAt(Object value, int index) {
			T typedValue = (T) value;
			values.add(index,typedValue);
		}
		
		@Override
		public ColumnDescriptor getDescriptor() {
			return descriptor;
		}
		
		public ColumnImpl<T> getFilteredInstance (BitSet bitSet) {
			int newLength = bitSet.cardinality();
			
			ArrayList<T> filteredValues = new ArrayList<>(newLength);
			 for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
				 filteredValues.add(getValueAt(i));
			 }
			 return new ColumnImpl<T>(descriptor,filteredValues);
		}
		
	}
	
	
	
	/*==== IDataSet implementation ====*/
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
		public boolean containsColumn(FieldDescriptor field) {
			String key = field.getKey();
			return columns.containsKey(key);
		}
		
		
	    void addColumn( ColumnImpl<?> newColumn) {
			String key = newColumn.getDescriptor().getKey();
			columns.put(key, newColumn);
		}
	    
	    
	    private void initializeValidityBitSet(int recordNum) {
	    	this.validityBitset = new BitSet(recordNum);
	    	validityBitset.set(1,recordNum,true);
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


		@Override
		public IColumn<?> getColumn(FieldDescriptor column) {
			String key = column.getKey();
			return columns.get(key);
		}


		@Override
		public List<IColumn<?>> getAllColumns() {
			return Lists.newArrayList(columns.values());
		}

		@Override
		public IDataSet getVerticalpartition(FieldDescriptor... field) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IRecordIterator getRecordIterator() {
			return new BaseRecordIterator(this);
		}

		@Override
		public int getRecordCount() {
			return recordCount;
		}

		@Override
		public int getFieldsCount() {
			return columns.size();
		}
		
	}

	
	
	/* ==== LayoutManager Implementation ==== */
	public BaseLayoutManager() {
		super();
	}
	
	
	private ColumnImpl<?> createColumn(ColumnDescriptor descriptor, int length) {
		ColumnImpl<?> newColumn = null;
		DataType type = descriptor.getColumnType();
		switch (type) {
		case INTEGER:
			newColumn = new ColumnImpl<Integer>(descriptor,length);
			break;
		case DOUBLE:
			newColumn = new ColumnImpl<Double>(descriptor,length);
			break;
		case FLOAT:
			newColumn = new ColumnImpl<Float>(descriptor,length);
			break;
		case LONG:
			newColumn = new ColumnImpl<Long>(descriptor,length);
			break;
		case STRING:
			newColumn = new ColumnImpl<String>(descriptor,length);
			break;
		case BIG_DECIMAL:
			newColumn = new ColumnImpl<BigDecimal>(descriptor,length);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return newColumn;
	}
	
	
	/*======METHODS INHERITED FROM ILayoutManager=====*/
	@Override
	public IDataSet buildDataSet(IRecordIterator iterator) {
		
		int recordCount = iterator.getRecordCount();
		BaseDataSet dataSet = new BaseDataSet(recordCount);
		
		/*
		 * For each field in iteraror metadata, create a new column
		*/
		int fieldsCount = iterator.getFieldsCount();
		ColumnImpl<?>[] columns = new ColumnImpl<?>[fieldsCount+1];
		for(int index = 1; index <= fieldsCount; index++) {
			
			DataType columnType = iterator.getColumnType(index);
			String columnName = iterator.getColumnName(index);
			String tableName = iterator.getTableName(index);
			int columnLength = recordCount;
			ColumnDescriptor descriptor  =
					new ColumnDescriptor(tableName, columnName, columnType);
			columns[index] = createColumn (descriptor,columnLength);
		}
		
		/*
		 * For each position, fill each column with corresponding value
		 */
		Object value;
		int columnIndex = 0;
		while(iterator.hasNext()) {
			iterator.next();
			for(int index = 1; index <= fieldsCount; index++) {
				value = iterator.getValueByColumnIndex(index);
				columns[index].storeValueAt(value, columnIndex);
			}
		}
		
		/*
		 * Add every column to the new dataset
		 */
		for(int index = 1; index <= fieldsCount; index++) {
			dataSet.addColumn(columns[index]);
		}
		return dataSet;
	}


	@Override
	public IDataSet mergeDatasets(List<IDataSet> dataSets) {
		BitSet mergedBitSet = mergeValidityBitsets(dataSets);
		IDataSet mergedDataSet = buildDataSet(dataSets,mergedBitSet);
		return mergedDataSet;
	}


	private BitSet mergeValidityBitsets(List<IDataSet> dataSets) {
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


	private IDataSet buildDataSet(List<IDataSet> dataSets, BitSet bitSet) {
		int length = bitSet.cardinality();
		BaseDataSet newDataSet = new BaseDataSet(length);
		
		for(IDataSet dataSet : dataSets) {
			for(IColumn<?> column : dataSet.getAllColumns()) {
				ColumnImpl<?> newColumn = 
						((ColumnImpl<?>)column).getFilteredInstance(bitSet);
				newDataSet.addColumn( newColumn);
			}
		}
		return newDataSet;
	}

}
