package model;

public interface IField {

	public Class getType ();
	
	public String getName();
	
	public boolean isPrimaryKey();
	
	public ITable getTable();
	
}
