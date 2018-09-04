package impl.query.execution.operator.orderby;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import utils.RecordComparator;

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
				
		Stream<Object[]> recordStream = inputSet.getRecordStream();
		
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		Map<String,Integer> nameIndexMapping = inputSet.getNameIndexMapping();
		Comparator<Object[]> recordComparator = 
				RecordComparator.getRecordComparator(nameIndexMapping,orderingSequence);
		
		Iterator<Object[]> orderedRecords = recordStream
				.sorted(recordComparator) 
				.collect(Collectors.toList())
				.iterator();

		IDataSet result = layoutManager.buildMaterializedDataSet(recordCount, columnSequence, orderedRecords);
		return result;
	}		
	
}