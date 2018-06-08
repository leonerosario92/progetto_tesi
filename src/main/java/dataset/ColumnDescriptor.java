package dataset;

import context.DataType;


public class ColumnDescriptor {
	private DataType columnType;
	private String columnName;
	private String  tableName;

	
	public  ColumnDescriptor(String tableName, String columnName, DataType columnType) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnType = columnType;
	}
	
	
	public String getKey () {
		return tableName+"_"+columnName;
	}
	
	
	public String getTableName() {
		return tableName;
	}

	
	public String getColumnName() {
		return columnName;
	}
	
	
	public DataType getColumnType() {
		return columnType;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ColumnDescriptor))return false;
	    ColumnDescriptor otherColumnDescriptor = (ColumnDescriptor)other;
		String otherKey = otherColumnDescriptor.getKey();
		String thisKey = getKey();	
		return thisKey.equals(otherKey);
	}
	
	
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}
	
}
