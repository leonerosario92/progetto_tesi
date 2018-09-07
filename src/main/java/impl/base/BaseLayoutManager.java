package impl.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.LayoutManager;
import datasource.IRecordScanner;
import datasource.ISizedRecordScanner;
import datatype.DataType;
import objectexplorer.MemoryMeasurer;
import utils.RecordEvaluator;
import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;

public class BaseLayoutManager extends LayoutManager {
	
	public BaseLayoutManager() {
		super();
	}
	
	
//	public IDataSet<Object[]> buildColumnarDataSet(ISizedRecordScanner recordScanner) {
//		
//		int recordCount = recordScanner.getRecordCount();
//		BaseDataSet dataSet = new BaseDataSet(recordCount);
//		
//		/*
//		 * For each field in iteraror metadata, create a new column
//		*/
//		int fieldsCount = recordScanner.getFieldsCount();
//		BaseColumn<?>[] columns = new BaseColumn<?>[fieldsCount+1];
//		for(int index = 1; index <= fieldsCount; index++) {
//			
//			DataType columnType = recordScanner.getColumnType(index);
//			String columnName = recordScanner.getColumnName(index);
//			String tableName = recordScanner.getTableName(index);
//			int columnLength = recordCount;
//			ColumnDescriptor descriptor  =
//					new ColumnDescriptor(tableName, columnName, columnType);
//			columns[index] = BaseColumnFactory.createColumn (descriptor,columnLength);
//		}
//		
//		/*
//		 * For each position, fill each column with corresponding value
//		 */
//		Object value;
//		int recordIndex = 0;
//		while(recordScanner.next()) {
//			for(int columnIndex = 1; columnIndex <= fieldsCount; columnIndex++) {
//				value = recordScanner.getValueByColumnIndex(columnIndex);
//				columns[columnIndex].storeValueAt(value, recordIndex);
//			}
//			recordIndex ++;
//		}
//		
//		/*
//		 * Add every column to the new dataset
//		 */
//		for(int index = 1; index <= fieldsCount; index++) {
//			dataSet.addColumn(columns[index]);
//		}
//		return dataSet;
//	}
//
//	public IDataSet<Object[]> mergeColumnarDatasets(Iterable<BaseDataSet> dataSets) {
//		BitSet mergedBitSet = mergeValidityBitsets(dataSets);
//		IDataSet<Object[]> mergedDataSet = materializeDataSet(dataSets,mergedBitSet);
//		return mergedDataSet;
//	}
//
//
//	private BitSet mergeValidityBitsets(Iterable<BaseDataSet> dataSets) {
//		BitSet mergedValidityBitSet = null;
//		Iterator<BaseDataSet> it = dataSets.iterator();
//		if(it.hasNext()) {
//			BaseDataSet next = it.next();
//			mergedValidityBitSet  = next.getValidityBitSet();
//		}
//		while(it.hasNext()) {
//			BitSet bitSet = it.next().getValidityBitSet();
//			if(mergedValidityBitSet.size() != bitSet.size()) {
//				throw new IllegalArgumentException("Only dataset with same size can be merged on validity bitSet");
//			}
//			mergedValidityBitSet.and(bitSet);
//		}
//		return mergedValidityBitSet;
//	}
//
//	
//	
//	private IDataSet<Object[]> materializeDataSet(Iterable<BaseDataSet> dataSets, BitSet bitSet) {
//		int recordCount = bitSet.cardinality();
//		List<Iterator<?>> columnIterators = new ArrayList<>();
//		List<ColumnDescriptor> descriptors = new ArrayList<>();
//		int columnCount = 0;
//		for(BaseDataSet dataSet : dataSets) {
//			for(IColumn<?> column : dataSet.getAllColumns()) {
//				IColumn<?> filteredColumn = ((BaseColumn<?>)column).getFilteredInstance(bitSet);
//				ColumnDescriptor descriptor = filteredColumn.getDescriptor();
//				descriptors.add(descriptor);
//				columnIterators.add(filteredColumn.getColumnIterator());
//				columnCount ++;
//			}
//		}
//		
//		MaterializedDataSet result = new MaterializedDataSet(descriptors);
//		for(int i=0; i<recordCount; i++) {
//			Object[] currentRecord = new Object[columnCount];
//			for(int j=0; j<columnCount; j++) {
//				currentRecord[j] = columnIterators.get(j).next();
//			}
//			result.addRecord(currentRecord);
//		}
//		return result;
//	}
	
	
	@Override
	public IDataSet buildMaterializedDataSet(IRecordScanner recordScanner) {
		List<ColumnDescriptor> columnSequence = getColumnSequence(recordScanner);
		MaterializedDataSet result = new MaterializedDataSet(columnSequence);
		while(recordScanner.next()) {
			Object[] currentRecord = recordScanner.getCurrentRecord();
			result.addRecord(currentRecord);
		}
		
		return result;
	}


	public IDataSet buildMaterializedDataSet(IRecordScanner recordScanner,RecordEvaluator recordEvaluator) {
		List<ColumnDescriptor> columnSequence = getColumnSequence(recordScanner);
		MaterializedDataSet result = new MaterializedDataSet(columnSequence);
		while(recordScanner.next()) {
			Object[] currentRecord = recordScanner.getCurrentRecord();
			if(recordEvaluator.evaluate(currentRecord)) {
				result.addRecord(currentRecord);
			}
		}
		return result;
	}

	
	@Override
	public IDataSet buildMaterializedDataSet(
			List<ColumnDescriptor> columnSequence, 
			Iterator<Object[]> recordIterator) 
	{
		MaterializedDataSet result = new MaterializedDataSet(columnSequence);
		while(recordIterator.hasNext()) {
			result.addRecord(recordIterator.next());
		}
		return result;
	}
	
	
	private List<ColumnDescriptor> getColumnSequence(IRecordScanner recordScanner) {
		List<ColumnDescriptor> columnSequence = new ArrayList<>();
		int fieldsCount = recordScanner.getFieldsCount();
		for(int index = 1; index <= fieldsCount; index++) {
			DataType columnType = recordScanner.getColumnType(index);
			String columnName = recordScanner.getColumnName(index);
			String tableName = recordScanner.getTableName(index);
			ColumnDescriptor descriptor  =
					new ColumnDescriptor(tableName, columnName, columnType);
			columnSequence.add(descriptor);
		}
		return columnSequence;
	}
	
	


	@Override
	public StreamPipeline buildStreamedDataSet(IRecordScanner recordScanner) {
		Stream <Object[]> recordStream = StreamSupport.stream(getRecordSpliterator(recordScanner),false);
		List<ColumnDescriptor> columnSequence = getColumnSequence(recordScanner);
		StreamPipeline result = new StreamPipeline(recordStream,columnSequence);
		return result;
	}


	private Spliterator<Object[]> getRecordSpliterator(IRecordScanner scanner) {
		int fieldsCount = scanner.getFieldsCount();
		Spliterator<Object[]> spliterator = 
			new Spliterators.AbstractSpliterator<Object[]>(
					0,Spliterator.IMMUTABLE|Spliterator.ORDERED) {
				@Override
				public boolean tryAdvance(Consumer<? super Object[]> action) {
					if(!scanner.next()) return false;
                    action.accept(recordFromScannerRow(scanner));
                    return true;
				}

				private Object[] recordFromScannerRow(IRecordScanner currentRow) {
					Object[] currentRecord = new Object[fieldsCount];
					for (int i=0; i<fieldsCount; i++) {
						currentRecord[i] = currentRow.getValueByColumnIndex(i+1);
					}
					return currentRecord;
				}
			};
		return spliterator;
	}

}
