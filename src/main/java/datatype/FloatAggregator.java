package datatype;

import java.util.DoubleSummaryStatistics;

public class FloatAggregator implements TypeAggregator<Float>{

	private DoubleSummaryStatistics aggregator;
	
	public FloatAggregator() {
		this.aggregator = new DoubleSummaryStatistics();
	}
	
	@Override
	public Float getMax() {
		return (float)aggregator.getMax();
	}

	@Override
	public Float getMin() {
		return (float)aggregator.getMin();
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
		aggregator.accept(Float.class.cast(value));
	}

	@Override
	public void combine(TypeAggregator<Float> other) {
		aggregator.combine(FloatAggregator.class.cast(other).aggregator);
	}

}
