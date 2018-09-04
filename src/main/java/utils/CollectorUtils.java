package utils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import model.AggregationDescriptor;

public class CollectorUtils {
	
	private CollectorUtils() {}
	
	public static Collector<Object[], RecordAggregator, Object[]> getRecordDownStreamCollector(
			List<AggregationDescriptor> aggregations, 
			Map<String,Integer> nameIndexMapping) 
	{
		Supplier<RecordAggregator> supplier = getSupplier(aggregations,nameIndexMapping);
		BiConsumer<RecordAggregator, Object[]> accumulator = getAccumulator();
		BinaryOperator<RecordAggregator> combiner = getCombiner();
		Function<RecordAggregator,Object[]> finalizer = getFinalizer(aggregations);
		
		return Collector.of(supplier,accumulator,combiner ,finalizer);
	}
	
	
	private static Supplier<RecordAggregator> getSupplier(
			List<AggregationDescriptor> aggregations,
			Map<String,Integer> nameIndexMapping) 
	{
		return new Supplier<RecordAggregator>() {
			@Override
			public RecordAggregator get() {
				return new RecordAggregator(aggregations, nameIndexMapping);
			}
		};
	}	
	
	
	private static BiConsumer<RecordAggregator, Object[]> getAccumulator() {
		return new BiConsumer<RecordAggregator, Object[]>() {
			@Override
			public void accept(RecordAggregator recordAggregator, Object[] record) {
				recordAggregator.aggregateRecord(record);
			}
		};
	}
	
	
	private static BinaryOperator<RecordAggregator> getCombiner() {
		return new BinaryOperator<RecordAggregator>() {
			@Override
			public RecordAggregator apply(RecordAggregator aggregator, RecordAggregator other) {
				RecordAggregator combinedAggregator = aggregator.combine(other);
				return combinedAggregator;
			}
		};
	}
	
	
	private static Function<RecordAggregator, Object[]> getFinalizer (List<AggregationDescriptor> aggregations) {
		return new Function<RecordAggregator, Object[]>() {
			@Override
			public Object[] apply(RecordAggregator aggregator) {
				return aggregator.getAggregationResult();
			}
		};
	}
	
	
	
}
