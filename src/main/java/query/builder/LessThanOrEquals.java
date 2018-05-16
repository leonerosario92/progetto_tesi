package query.builder;

public class LessThanOrEquals extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.LESS_THAN_OR_EQUAL;
	
	public LessThanOrEquals() {
		super(type);
	}
}
