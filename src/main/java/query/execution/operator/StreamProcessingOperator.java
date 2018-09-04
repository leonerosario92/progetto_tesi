package query.execution.operator;

import java.util.Map;
import java.util.stream.Stream;

import query.ImplementationProvider;

public class StreamProcessingOperator <F extends StreamProcessingFunction, A extends IOperatorArgs> 
extends StreamOperator<F,A> 
{
	
	public StreamProcessingOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider,type);
	}
	
	public Stream<Object[]> addOperationToPipeline(Stream<Object[]> inputStream, Map<String,Integer> mapping){
		return function.apply(inputStream,mapping,args);
	}
}
