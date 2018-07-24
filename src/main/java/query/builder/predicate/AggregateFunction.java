package query.builder.predicate;

import query.builder.QueryConstants;

public enum AggregateFunction {
	
	SUM(QueryConstants.SUM),
	AVG(QueryConstants.AVG),
	COUNT(QueryConstants.COUNT),
	MIN(QueryConstants.MIN),
	MAX(QueryConstants.MAX);

	private String representation;
	
	public String getRepresentation() {
		return this.representation;
	}
	
	AggregateFunction(String representation){
		this.representation = representation;
	}
	
	
}
