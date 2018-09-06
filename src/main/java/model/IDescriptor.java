
package model;

import datatype.DataType;

public interface IDescriptor {

	public String getName();
	
	public String getKey();

	public TableDescriptor getTable();
	
	public DataType getType();
	
}
