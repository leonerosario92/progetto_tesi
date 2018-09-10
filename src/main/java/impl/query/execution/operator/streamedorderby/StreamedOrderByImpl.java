package impl.query.execution.operator.streamedorderby;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import dataset.IRecordMapper;
import impl.base.StreamPipeline;
import model.FieldDescriptor;
import model.IDescriptor;
import query.execution.operator.streamedorderby.StreamedOrderByArgs;
import query.execution.operator.streamedorderby.StreamedOrderByFunction;
import utils.RecordComparator;

public class StreamedOrderByImpl extends StreamedOrderByFunction{

	@Override
	public StreamPipeline apply(
			StreamPipeline pipeline, 
			StreamedOrderByArgs args) 
	{
		Stream<Object[]> recordStream = pipeline.getRecordStream();
		IRecordMapper recordMapper = pipeline.getRecordMapper();
		
		List<FieldDescriptor> orderingSequence = args.getOrderingSequence();
		Comparator<Object[]> recordComparator = 
				RecordComparator.getRecordComparator(recordMapper, orderingSequence);
		Stream<Object[]> sortedStream = recordStream.sorted(recordComparator);
		pipeline.updateStream(sortedStream);
		return pipeline;
	}

}
