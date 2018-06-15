package impl.base;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import model.FieldDescriptor;

/*==== IDataSet implementation ====*/
public class BaseDataSet implements IDataSet {
	
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
    	validityBitset.set(0,recordNum,true);
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
