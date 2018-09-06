package query.execution.operator;

import java.util.Map;
import java.util.stream.Stream;

import impl.base.StreamPipeline;
import query.ImplementationProvider;

public class StreamProcessingOperator <F extends StreamProcessingFunction, A extends IOperatorArgs> 
extends StreamOperator<F,A> 
{
	
	public StreamProcessingOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider,type);
	}
	
	public StreamPipeline addOperationToPipeline(StreamPipeline pipeline){
		return function.apply(pipeline,args);
	}
}
