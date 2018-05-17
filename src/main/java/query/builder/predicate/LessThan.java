package query.builder.predicate;

import query.builder.statement.FilterStatement;

public class LessThan extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.LESS_THAN;
	
	public LessThan() {
		super(type);
	}

}
