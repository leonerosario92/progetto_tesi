package datatype;

import query.builder.predicate.AggregateFunction;

public interface TypeAggregator<T> {
	
	void addValue(Object value);
	
	public void combine(TypeAggregator<?> typeAggregator);

	public Double getAggregationResult(AggregateFunction aggrType);

}
