package model;

import java.util.Set;

public interface IMetaData {

	public Set<ITable> getTables ();
	
	public ITable getTable (String tableId);
	
	public Set<IRelation> getRelations();
	
	public Set<IRelation> getRelations(String srcTableId, String dstTableId);
	
}

