package query.builder;

import model.TableDescriptor;

public class SelectStatement {
	
	private TableDescriptor table;
	
	public SelectStatement(TableDescriptor table) {
		this.table = table;
	}
	
	public TableDescriptor getTable() {
		return table;
	}
}
