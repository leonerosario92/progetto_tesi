package query.builder;

public class GreaterThanOrEqual extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.GREATER_THAN;
	
	public GreaterThanOrEqual() {
		super(type);
	}

}
