package impl.base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;

import dataset.ColumnDescriptor;
import dataset.IColumn;
import dataset.IDataSet;
import dataset.IRecordIterator;
import datasource.IRecordScanner;
import model.FieldDescriptor;

/*==== IDataSet implementation ====*/
public class BaseDataSet implements IDataSet,Iterable<Object[]> {
	
	private List<BaseColumn<?>> columns;
	private Map<String,Integer> nameindexMapping;
	private BitSet validityBitset;
	private int recordCount;
	
	public BaseDataSet(int recordCount){
		this.recordCount = recordCount;
		this.columns = new ArrayList<>();
		nameindexMapping = new HashMap<>();
		initializeValidityBitSet();
	}
	
	
    void addColumn(BaseColumn<?> newColumn) {
		String key = newColumn.getDescriptor().getKey();
		int columnIndex = columns.size()+1;
		columns.add(columnIndex,newColumn);
		nameindexMapping.put(key,columnIndex);
	}
    
    
    private void initializeValidityBitSet() {
    	this.validityBitset = new BitSet(recordCount);
    	validityBitset.set(0,recordCount,true);
    }

    
    @Override
    public boolean containsColumn(FieldDescriptor field) {
    	String key = field.getKey();
    	return nameindexMapping.containsKey(key);
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
		int index = nameindexMapping.get(key);
		return columns.get(index);
	}


	@Override
	public List<IColumn<?>> getAllColumns() {
		return Lists.newArrayList(columns);
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

	
	@Override
	public IRecordScanner getRecordScanner() {
		return new BaseRecordScanner(this);
	}
		
	
	@Override
	public Iterator<Object[]> iterator() {
		return new BaseRecordIterator(this);
	}
	
	
	@Override
	public ColumnDescriptor getColumnDescriptor(int index) {
		return columns.get(index).getDescriptor();
	}

	@Override
	public int getColumnIndex(FieldDescriptor field) {
		return nameindexMapping.get(field.getKey());
	}


	@Override
	public Map<String, Integer> getNameIndexMapping() {
		return nameindexMapping;
	}

}
