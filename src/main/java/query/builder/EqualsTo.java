package query.builder;

public class EqualsTo extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.EQUALS_TO;
	
	public EqualsTo() {
		super(type);
	}
}