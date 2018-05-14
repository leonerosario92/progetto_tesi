package model;

public class FieldDescriptor implements IFieldDescriptor{
	
	private Class<?> type;
	private String name;
	private boolean isPrimaryKey;
	private ITableDescriptor table;

	
	public FieldDescriptor(ITableDescriptor table, String name, Class<?> type, boolean isPrimaryKey) {
		this.table = table;
		this.name = name;
		this.type = type;	
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public ITableDescriptor getTable() {
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
