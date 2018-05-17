package query.builder.predicate;

import query.builder.statement.FilterStatement;

public class EqualsTo extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.EQUALS_TO;
	
	public EqualsTo() {
		super(type);
	}
}