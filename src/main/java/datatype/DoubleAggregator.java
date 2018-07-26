package datatype;

import java.util.DoubleSummaryStatistics;
import java.util.Optional;

import query.builder.predicate.AggregateFunction;

public class DoubleAggregator implements TypeAggregator<Double> {

	private DoubleSummaryStatistics aggregator;
	
	public DoubleAggregator() {
		this.aggregator = new DoubleSummaryStatistics();
	}

	@Override
	public void addValue(Object value) {
		aggregator.accept(Double.class.cast(value));
	}

	@Override
	public void combine(TypeAggregator<?> other) {
		aggregator.combine(DoubleAggregator.class.cast(other).aggregator);
	}

	@Override
	public Double getAggregationResult(AggregateFunction aggrType) {
		Number result = null;
		switch(aggrType) {
		case AVG:
			result = aggregator.getAverage();
			break;
		case COUNT:
			result = aggregator.getCount();
			break;
		case MAX:
			result = aggregator.getMax();
			break;
		case MIN:
			result = aggregator.getMin();
			break;
		case SUM: 
			result = aggregator.getSum();
			break;
		default:
			throw new IllegalArgumentException();
		}
		return (double) result;
	}

}
