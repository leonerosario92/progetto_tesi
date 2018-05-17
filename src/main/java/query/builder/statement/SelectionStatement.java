package query.builder.statement;

import model.TableDescriptor;

public class SelectionStatement {
	
	private TableDescriptor table;
	
	public SelectionStatement(TableDescriptor table) {
		this.table = table;
	}
	
	public TableDescriptor getTable() {
		return table;
	}
}
