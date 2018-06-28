package query.builder;

public enum LogicalOperand {
	
	AND (QueryConstants.AND),
	OR (QueryConstants.OR);
	
	private String sqlRepresentation;
	LogicalOperand(String sqlRepresentation) {
		this.sqlRepresentation = sqlRepresentation;
	}
	
	public Object getSqlRepresentation() {
		return sqlRepresentation;
	}
}
