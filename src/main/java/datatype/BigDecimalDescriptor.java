package datatype;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;


import query.builder.predicate.AggregateFunction;



	public class BigDecimalDescriptor implements NumericTypeDescriptor{

		@Override
		public boolean isComparable() {
			return true;
		}

		@Override
		public TypeComparator getTypeComparator() {
			return new BigDecimalComparator();
		}

		@Override
		public boolean isAggregable() {
			return true;
		}

		@Override
		public TypeAggregator getTypeAggregator() {
			return new BigDecimalAggregator();
		}


		@Override
		public Number getValueAsNumber(Object value) {
			return BigDecimal.class.cast(value);
		}
		
	
	
	private class BigDecimalAggregator implements TypeAggregator<BigDecimal>{

		private DoubleSummaryStatistics aggregator;
		
		public BigDecimalAggregator() {
			this.aggregator = new DoubleSummaryStatistics();
		}
		
		@Override
		public void addValue(Object value) {
			aggregator.accept(BigDecimal.class.cast(value).doubleValue());
		}

		@Override
		public void combine(TypeAggregator<?> other) {
			this.aggregator.combine(BigDecimalAggregator.class.cast(other).aggregator);
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
}
