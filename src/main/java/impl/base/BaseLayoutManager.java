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
