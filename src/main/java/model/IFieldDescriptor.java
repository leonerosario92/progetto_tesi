package model;

public interface IFieldDescriptor {

	public Class getType ();
	
	public String getName();
	
	public boolean isPrimaryKey();
	
	public ITableDescriptor getTable();
	
}
