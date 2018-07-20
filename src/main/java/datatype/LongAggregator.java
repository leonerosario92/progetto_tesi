package datatype;

import java.util.LongSummaryStatistics;
import java.util.Optional;

public class LongAggregator implements TypeAggregator<Long> {

	private LongSummaryStatistics aggregator;
	
	public LongAggregator() {
		aggregator = new LongSummaryStatistics();
	}
	
	@Override
	public Long getMax() {
		return aggregator.getMax();
	}

	@Override
	public Long getMin() {
		return aggregator.getMin();
	}

	@Override
	public Double getSum() {
		return Double.valueOf(aggregator.getSum());
	}

	@Override
	public Double getAverage() {
		return Double.valueOf(aggregator.getAverage());
	}

	@Override
	public Long getCount() {
		return aggregator.getCount();
	}

	@Override
	public void addValue(Object value) {
		aggregator.accept(Long.class.cast(value));
	}

	@Override
	public void combine(TypeAggregator<Long> other) {
		aggregator.combine(LongAggregator.class.cast(other).aggregator);
	}

}
