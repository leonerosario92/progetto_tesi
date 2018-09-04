package impl.query.execution.operator.streamedorderby;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import datatype.TypeComparator;
import model.FieldDescriptor;
import query.execution.operator.streamedorderby.StreamedOrderByArgs;
import query.execution.operator.streamedorderby.StreamedOrderByFunction;
import utils.RecordComparator;

public class StreamedOrderByImpl extends StreamedOrderByFunction{

	@Override
	public Stream<Object[]> apply(Stream<Object[]> inputStream, 
			Map<String, Integer> nameIndexMapping,
			StreamedOrderByArgs args) 
	{
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		Comparator<Object[]> recordComparator = 
				RecordComparator.getRecordComparator(nameIndexMapping, orderingSequence);
		Stream<Object[]> sortedStream = inputStream.sorted(recordComparator);
		return sortedStream;
	}

}
