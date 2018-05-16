package query.builder;

public class LessThan extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.LESS_THAN;
	
	public LessThan() {
		super(type);
	}

}
