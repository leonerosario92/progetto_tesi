package query.builder.predicate;

import query.builder.statement.FilterStatement;

public class DifferentFrom extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.DIFFERENT_FROM;
	
	public DifferentFrom() {
		super(type);
	}
}