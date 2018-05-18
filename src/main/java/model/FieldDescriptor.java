package model;

public class FieldDescriptor {
	
	private Class<?> type;
	private String name;
	private boolean isPrimaryKey;
	private TableDescriptor table;

	
	public FieldDescriptor(TableDescriptor table, String name, Class<?> type, boolean isPrimaryKey) {
		this.table = table;
		this.name = name;
		this.type = type;	
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public TableDescriptor getTable() {
		return table;
	}

	public Class<?> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
}