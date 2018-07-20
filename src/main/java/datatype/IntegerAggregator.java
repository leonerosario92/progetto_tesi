package datatype;

import java.util.IntSummaryStatistics;
import java.util.Optional;

public class IntegerAggregator implements TypeAggregator<Integer> {

	private IntSummaryStatistics aggregator;
	
	public IntegerAggregator() {
		aggregator = new IntSummaryStatistics();
	}
	
	@Override
	public Integer getMax() {
		return aggregator.getMax();
	}

	@Override
	public Integer getMin() {
		return aggregator.getMin();
	}

	@Override
	public Double getSum() {
		return Double.valueOf( aggregator.getSum());
	}
	
	@Override
	public Double getAverage() {
		return Double.valueOf( aggregator.getAverage());
	}

	@Override
	public Long getCount() {
		return Long.valueOf(aggregator.getCount());
	}

	@Override
	public void addValue(Object value) {
		aggregator.accept((int) value);
	}

	@Override
	public void combine(TypeAggregator<Integer> other) {
		aggregator.combine(IntegerAggregator.class.cast(other).aggregator);
	}

}
