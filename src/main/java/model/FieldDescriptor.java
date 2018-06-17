package model;

import datatype.DataType;

public class FieldDescriptor {
	
	private DataType type;
	private String name;
	private boolean isPrimaryKey;
	private TableDescriptor table;

	
	public FieldDescriptor(TableDescriptor table, String name, DataType type, boolean isPrimaryKey) {
		this.table = table;
		this.name = name;
		this.type = type;	
		this.isPrimaryKey = isPrimaryKey;
	}
	
	
	public TableDescriptor getTable() {
		return table;
	}

	
	public DataType getType() {
		return type;
	}

	
	public String getName() {
		return name;
	}

	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
	
	public String getKey () {
		return getTable().getName()+"_"+getName();
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof FieldDescriptor))return false;
	    FieldDescriptor otherFieldDescriptor = (FieldDescriptor)other;
		String otherKey = otherFieldDescriptor.getKey();
		String thisKey = getKey();	
		return thisKey.equals(otherKey);
	}
	
	
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}
	
}
