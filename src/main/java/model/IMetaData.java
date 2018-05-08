package model;

import java.util.Set;

public interface IMetaData {

	public Iterable<ITable> getTables ();
	
	public ITable getTable (String tableId);
	
	public Iterable<IRelation> getAllRelations();
	
	public Iterable<IRelation> getRelations(String tableId);
	
	public Iterable<IRelation> getRelations(String srcTableId, String dstTableId);
	
}

