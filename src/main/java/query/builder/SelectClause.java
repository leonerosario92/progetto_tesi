package query.builder;

import java.util.HashSet;
import java.util.Set;

import model.TableDescriptor;

public class SelectClause {
	
	private HashSet<TableDescriptor> selectedTables;
	
	public SelectClause () {
		selectedTables = new HashSet<>();
	}
	
	public void addStatement(SelectStatement statement) {
		if(selectedTables.contains(statement.getTable())) {
			//TODO Manage exception properly
			throw new IllegalArgumentException();
		}
	}
}
