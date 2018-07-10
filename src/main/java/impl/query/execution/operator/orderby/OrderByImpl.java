package impl.query.execution.operator.orderby;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datatype.TypeComparator;
import impl.base.BaseDataSet;
import model.FieldDescriptor;
import query.execution.QueryExecutionException;
import query.execution.operator.orderby.OrderByArgs;
import query.execution.operator.orderby.OrderByFunction;

public class OrderByImpl extends OrderByFunction {

	@Override
	public IDataSet apply(Iterable<IDataSet> inputDataSets, ILayoutManager layoutManager, OrderByArgs args) {

		/* TODO Find a way to pass only one dataset to the function*/
		Iterator<IDataSet> it = inputDataSets.iterator();
		IDataSet inputSet= null;
		if(it.hasNext()) {
			inputSet = it.next();
		}
		if(inputSet == null) {
			throw new IllegalStateException("Order by function requires only one inupt dataset");
		}
		/*_________________________________________________________*/
		
		
		IRecordIterator iterator = inputSet.getRecordIterator();
		int fieldsCount = inputSet.getFieldsCount();
		int recordCount = inputSet.getRecordCount();
		
		Stream<Object[]> recordStream = 
				getRecordStream(iterator,recordCount);
		
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		
		Comparator<Object[]> recordComparator = getRecordComparator(inputSet,orderingSequence);
		
		long start = System.nanoTime();
		Iterator<Object[]> resultIterator = recordStream
				.sorted(recordComparator) 
				.collect(Collectors.toList())
				.iterator();
		
		long end = System.nanoTime();
		float timeMillis = ((float)(end -start))/(1000*1000); 
		
		return inputSet;
		
	}
	

	private Stream<Object[]> getRecordStream(IRecordIterator recordIterator, int recordCount) {
		Stream<Object[]> result = StreamSupport.stream
		(
			Spliterators.spliterator(recordIterator,recordCount, 0)
			,false
		);
		return result;
	}
	
	
	private Comparator<Object[]> getRecordComparator(
			IDataSet inputSet, 
			List<FieldDescriptor> orderingSequence
	){
		TypeComparator[] comparators = new TypeComparator [orderingSequence.size()];
		int [] indexes = new int[orderingSequence.size()];
		for(int i=0; i<orderingSequence.size(); i++) {
			indexes[i] = inputSet.getColumnIndex(orderingSequence.get(i));
			comparators[i] = 
					inputSet.getColumnDescriptor(indexes[i])
					.getColumnType()
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