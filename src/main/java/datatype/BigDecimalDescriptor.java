package datatype;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;



	public class BigDecimalDescriptor implements TypeDescriptor{

		@Override
		public boolean isComparable() {
			return true;
		}

		@Override
		public Optional<TypeComparator> getTypeComparator() {
			return Optional.of(new BigDecimalComparator());
		}

		@Override
		public boolean isAggregable() {
			return true;
		}

		@Override
		public Optional<TypeAggregator> getTypeAggregator() {
			return Optional.of(new BigDecimalAggregator());
		}

		@Override
		public boolean isNumber() {
			return true;
		}

		@Override
		public Optional<Number> getValueAsNumber(Object value) {
			return Optional.of(BigDecimal.class.cast(value));
		}

		@Override
		public boolean isString() {
			return false;
		}

		@Override
		public Optional<String> getValueAsString(Object value) {
			return Optional.empty();
		}
		
	
	
	private class BigDecimalAggregator implements TypeAggregator<BigDecimal>{

		private DoubleSummaryStatistics aggregator;
		
		public BigDecimalAggregator() {
			this.aggregator = new DoubleSummaryStatistics();
		}
		
		@Override
		public BigDecimal getMax() {
			return BigDecimal.valueOf(aggregator.getMax());
		}

		@Override
		public BigDecimal getMin() {
			return BigDecimal.valueOf(aggregator.getMin());
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
			aggregator.accept(BigDecimal.class.cast(value).doubleValue());
		}

		@Override
		public void combine(TypeAggregator<BigDecimal> other) {
			 aggregator.combine( BigDecimalAggregator.class.cast(other).aggregator );
		}
		
	}
}
