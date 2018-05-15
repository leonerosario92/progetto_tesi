package query.builder;

public enum PredicateType {
	GREATER_THAN (QueryConstants.GREATER_THAN), 
	GREATER_THAN_OR_EQUAL(QueryConstants.GREATER_THAN_OR_EQUALS),
	LESS_THAN (QueryConstants.LESS_THAN), 
	LESS_THAN_OR_EQUAL(QueryConstants.LESS_THAN_OR_EQUALS),
	EQUALS_TO(QueryConstants.EQUALS_TO),
	DIFFERENT_FROM(QueryConstants.DIFFERENT_FROM);
	
	public String representation;
	PredicateType (String representation) {
		this.representation = representation;
	}
}
