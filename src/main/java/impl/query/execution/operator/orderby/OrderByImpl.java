package impl.query.execution.operator.orderby;

import java.util.Comparator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.IDataSet;
import dataset.IRecordIterator;
import datatype.TypeComparator;
import impl.base.BaseDataSet;
import model.FieldDescriptor;
import query.execution.QueryExecutionException;
import query.execution.operator.orderby.OrderByArgs;
import query.execution.operator.orderby.OrderByFunction;

public class OrderByImpl extends OrderByFunction {

	@Override
	public IDataSet apply(IDataSet inputSet, OrderByArgs args) throws QueryExecutionException {
		
		IRecordIterator iterator = inputSet.getRecordIterator();
		int fieldsCount = iterator.getFieldsCount();
		
		Stream<Object[]> recordStream = 
				getRecordStream(iterator);
		
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		
		Comparator<Object[]> recordComparator = getRecordComparator(iterator,orderingSequence);
		
//		long start = System.nanoTime();
		List<Object[]> l = recordStream
				.sorted(recordComparator).collect(Collectors.toList());
//		long end = System.nanoTime();
//		float timeMillis = ((float)(end -start))/(1000*1000); 
		 
		return inputSet;
	}


	private Stream<Object[]> getRecordStream(IRecordIterator recordIterator) {
		Stream<Object[]> result = StreamSupport.stream
		(
			Spliterators.spliterator(recordIterator,recordIterator.getRecordCount(), 0)
			,false
		);
		return result;
	}
	
	
	private Comparator<Object[]> getRecordComparator(IRecordIterator recordIterator, List<FieldDescriptor> orderingSequence){
		TypeComparator[] comparators = new TypeComparator [orderingSequence.size()];
		int [] indexes = new int[orderingSequence.size()];
		for(int i=0; i<orderingSequence.size(); i++) {
			indexes[i] = recordIterator.getColumnIndex(orderingSequence.get(i).getName());
			comparators[i] = 
					recordIterator.getColumnType(indexes[i])
					.getComparator();		
		}
		
		Comparator<Object[]> recordComparator = (r,o)-> comparators[0].compare(r[indexes[0]], o[indexes[0]]);
		IntStream.range(0, orderingSequence.size())
		.forEach
		(
			i -> recordComparator.thenComparing
				(
					(r,o)-> comparators[i].compare(r[indexes[i]], o[indexes[i]]
				)
			)
		);
		return recordComparator;
	}
	
}