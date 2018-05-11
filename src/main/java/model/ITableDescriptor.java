package model;

import java.util.Set;

public interface ITableDescriptor {
	
	public Iterable <IFieldDescriptor> getFields();
	
	public String getName();
	
}
