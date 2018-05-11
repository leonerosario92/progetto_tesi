package model;

import java.util.Set;

public interface IMetaData {

	public Iterable<ITableDescriptor> getTables ();
	
	public ITableDescriptor getTable (String tableId);
	
	public Iterable<IRelationDescriptor> getAllRelations();
	
	public Iterable<IRelationDescriptor> getRelations(String tableId);
	
	public Iterable<IRelationDescriptor> getRelations(String srcTableId, String dstTableId);
	
}

