package impl.query.execution.operator.streamedrecordfilter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import impl.base.StreamPipeline;
import query.builder.statement.CFNode;
import query.execution.operator.streamedrecordfilter.FilterOnStreamArgs;
import query.execution.operator.streamedrecordfilter.FilterOnStreamFunction;
import utils.RecordEvaluator;

public class FilterOnStreamImpl extends FilterOnStreamFunction {

	@Override
	public StreamPipeline apply(
			StreamPipeline pipeline, 
			FilterOnStreamArgs args)
	{
		Stream<Object[]> recordStream = pipeline.getRecordStream();
		Map<String,Integer> nameIndexMapping = pipeline.getNameIndexMapping();
		
		Set<CFNode> statements = args.getStatements();
		RecordEvaluator evaluator = 
				new RecordEvaluator(nameIndexMapping, statements);
		Stream<Object[]> resultStream = 
				recordStream.filter( record -> evaluator.evaluate(record));
		pipeline.updateStream(resultStream);
		return pipeline;
	}



}
