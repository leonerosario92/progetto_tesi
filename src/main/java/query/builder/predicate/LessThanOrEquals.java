package query.builder.predicate;

import query.builder.statement.FilterStatement;

public class LessThanOrEquals extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.LESS_THAN_OR_EQUAL;
	
	public LessThanOrEquals() {
		super(type);
	}
}
