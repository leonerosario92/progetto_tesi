package query.builder.predicate;

import query.builder.statement.FilterStatement;

public class GreaterThanOrEqual extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.GREATER_THAN;
	
	public GreaterThanOrEqual() {
		super(type);
	}

}
