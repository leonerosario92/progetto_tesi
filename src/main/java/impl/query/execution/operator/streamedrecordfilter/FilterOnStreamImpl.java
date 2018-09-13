package impl.query.execution.operator.streamedrecordfilter;

import java.util.Set;
import java.util.stream.Stream;

import dataset.IRecordMapper;
import impl.base.StreamedDataSet;
import query.builder.statement.CFNode;
import query.execution.operator.streamedrecordfilter.FilterOnStreamArgs;
import query.execution.operator.streamedrecordfilter.FilterOnStreamFunction;
import utils.RecordEvaluator;

public class FilterOnStreamImpl extends FilterOnStreamFunction {

	@Override
	public StreamedDataSet apply(
			StreamedDataSet pipeline, 
			FilterOnStreamArgs args)
	{
		Stream<Object[]> recordStream = pipeline.getRecordStream();
		IRecordMapper recordMapper = pipeline.getRecordMapper();
		
		Set<CFNode> statements = args.getStatements();
		RecordEvaluator evaluator = 
				new RecordEvaluator(recordMapper, statements);
		Stream<Object[]> resultStream = 
				recordStream.filter( record -> evaluator.evaluate(record));
		pipeline.updateStream(resultStream);
		return pipeline;
	}

}
