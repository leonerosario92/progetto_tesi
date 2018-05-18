package model;

import java.util.Set;

public interface IMetaData {

	public Iterable<TableDescriptor> getTables ();
	
	public TableDescriptor getTable (String tableId);
	
	public Iterable<RelationDescriptor> getAllRelations();
	
	public Iterable<RelationDescriptor> getRelations(String tableId);
	
	public Iterable<RelationDescriptor> getRelations(String srcTableId, String dstTableId);
	
}

