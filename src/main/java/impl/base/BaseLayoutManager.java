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

import dataset.LayoutManager;
import datasource.IRecordScanner;
import datatype.DataType;
import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import model.FieldDescriptor;

public class BaseLayoutManager extends LayoutManager {
	
	public BaseLayoutManager() {
		super();
	}
	
	
	
	@Override
	public IDataSet buildDataSet(IRecordScanner recordScanner) {
		
		int recordCount = recordScanner.getRecordCount();
		BaseDataSet dataSet = new BaseDataSet(recordCount);
		
		/*
		 * For each field in iteraror metadata, create a new column
		*/
		int fieldsCount = recordScanner.getFieldsCount();
		BaseColumn<?>[] columns = new BaseColumn<?>[fieldsCount+1];
		for(int index = 1; index <= fieldsCount; index++) {
			
			DataType columnType = recordScanner.getColumnType(index);
			String columnName = recordScanner.getColumnName(index);
			String tableName = recordScanner.getTableName(index);
			int columnLength = recordCount;
			ColumnDescriptor descriptor  =
					new ColumnDescriptor(tableName, columnName, columnType);
			columns[index] = BaseColumnFactory.createColumn (descriptor,columnLength);
		}
		
		/*
		 * For each position, fill each column with corresponding value
		 */
		Object value;
		int columnIndex = 0;
		
		while(recordScanner.next()) {
			for(int index = 1; index <= fieldsCount; index++) {
				value = recordScanner.getValueByColumnIndex(index);
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
	public IDataSet mergeDatasets(Iterable<IDataSet> dataSets) {
		BitSet mergedBitSet = mergeValidityBitsets(dataSets);
		IDataSet mergedDataSet = buildDataSet(dataSets,mergedBitSet);
		return mergedDataSet;
	}


	private BitSet mergeValidityBitsets(Iterable<IDataSet> dataSets) {
		BitSet mergedValidityBitSet = null;
		Iterator<IDataSet> it = dataSets.iterator();
		if(it.hasNext()) {
			
			IDataSet next = it.next();
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


	private IDataSet buildDataSet(Iterable<IDataSet> dataSets, BitSet bitSet) {
		int length = bitSet.cardinality();
		BaseDataSet newDataSet = new BaseDataSet(length);
		
		for(IDataSet dataSet : dataSets) {
			for(IColumn<?> column : dataSet.getAllColumns()) {
				BaseColumn<?> newColumn = 
						((BaseColumn<?>)column).getFilteredInstance(bitSet);
				newDataSet.addColumn( newColumn);
			}
		}
		return newDataSet;
	}

}
