package impl.query.execution.operator.streamedrecordfilter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import query.builder.statement.CFNode;
import query.execution.operator.streamedrecordfilter.FilterOnStreamArgs;
import query.execution.operator.streamedrecordfilter.FilterOnStreamFunction;
import utils.RecordEvaluator;

public class FilterOnStreamImpl extends FilterOnStreamFunction {

	@Override
	public Stream<Object[]> apply(
			Stream<Object[]> inputStream, 
			Map<String, Integer> nameIndexMapping,
			FilterOnStreamArgs args)
	{
		Set<CFNode> statements = args.getStatements();
		RecordEvaluator evaluator = 
				new RecordEvaluator(nameIndexMapping, statements);
		Stream<Object[]> resultStream = 
				inputStream.filter( record -> evaluator.evaluate(record));
		return resultStream;
	}



}
