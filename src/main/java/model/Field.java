package model;

public class Field implements IField{
	
	private Class type;
	private String name;
	private boolean isPrimaryKey;
	private ITable table;

	
	public Field(ITable table, String name, Class type) {
		this.table = table;
		this.name = name;
		this.type = type;	
		this.isPrimaryKey = false;
	}
	
	public ITable getTable() {
		return table;
	}

	public Class getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
}
