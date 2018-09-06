package query.execution.operator;

import java.util.Map;
import impl.base.StreamPipeline;

public interface StreamProcessingFunction<A> extends IStreamedOperatorFunction {
	
	public StreamPipeline apply(
			StreamPipeline inputStream,
			A args
	);
	
}
