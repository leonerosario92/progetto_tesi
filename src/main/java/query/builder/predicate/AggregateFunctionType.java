package query.builder.predicate;

import model.FieldDescriptor;
import query.builder.QueryConstants;

public enum AggregateFunctionType {
	
	SUM(QueryConstants.SUM),
	AVG(QueryConstants.AVG),
	COUNT(QueryConstants.COUNT),
	MIN(QueryConstants.MIN),
	MAX(QueryConstants.MAX),
	DISTINCT(QueryConstants.DISTINCT);

	public String representation;
	
	
	AggregateFunctionType(String representation){
		this.representation = representation;
	}
	
	
}
