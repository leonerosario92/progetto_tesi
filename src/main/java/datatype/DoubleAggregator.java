package datatype;

import java.util.DoubleSummaryStatistics;
import java.util.Optional;

public class DoubleAggregator implements TypeAggregator<Double> {

	private DoubleSummaryStatistics aggregator;
	
	public DoubleAggregator() {
		this.aggregator = new DoubleSummaryStatistics();
	}
	
	@Override
	public Double getMax() {
		return aggregator.getMax();
	}

	@Override
	public Double getMin() {
		return aggregator.getMin();
	}

	@Override
	public Double getSum() {
		return aggregator.getSum();
	}

	@Override
	public Double getAverage() {
		return aggregator.getAverage();
	}

	@Override
	public Long getCount() {
		return aggregator.getCount();
	}

	@Override
	public void addValue(Object value) {
		aggregator.accept(Double.class.cast(value));
	}

	@Override
	public void combine(TypeAggregator<Double> other) {
		aggregator.combine(DoubleAggregator.class.cast(other).aggregator);
	}

}
