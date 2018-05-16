package query.builder;

public class DifferentFrom extends FilterStatement {

	private static FilterStatementType type = FilterStatementType.DIFFERENT_FROM;
	
	public DifferentFrom() {
		super(type);
	}
}