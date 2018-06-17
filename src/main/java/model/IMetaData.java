package model;

import java.util.Set;

public interface IMetaData {

	public Iterable<TableDescriptor> getTables ();
	
	public TableDescriptor getTable (String tableId);
	
}

