package model;

import context.DataType;

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
	
}
