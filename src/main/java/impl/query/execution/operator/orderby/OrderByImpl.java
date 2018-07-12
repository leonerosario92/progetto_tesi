package impl.query.execution.operator.orderby;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dataset.ColumnDescriptor;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datatype.TypeComparator;
import model.FieldDescriptor;
import query.execution.operator.orderby.OrderByArgs;
import query.execution.operator.orderby.OrderByFunction;

public class OrderByImpl extends OrderByFunction {

	@Override
	public IDataSet apply(IDataSet inputSet, ILayoutManager layoutManager, OrderByArgs args) {

		IRecordIterator iterator = inputSet.getRecordIterator();
		int fieldsCount = inputSet.getFieldsCount();
		int recordCount = inputSet.getRecordCount();
		
		List<ColumnDescriptor> columnSequence = new ArrayList<>();
		for(int i=0; i<fieldsCount; i++) {
			columnSequence.add(inputSet.getColumnDescriptor(i));
		}
				
		Stream<Object[]> recordStream = 
				getRecordStream(iterator,recordCount);
		
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		
		Comparator<Object[]> recordComparator = getRecordComparator(inputSet,orderingSequence);
		
		Iterator<Object[]> orderedRecords = recordStream
				.sorted(recordComparator) 
				.collect(Collectors.toList())
				.iterator();

		IDataSet result = layoutManager.buildDataSet(recordCount, columnSequence, orderedRecords);
		return result;
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