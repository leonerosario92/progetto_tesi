package query.execution.operator;

import java.util.Map;
import impl.base.StreamedDataSet;

public interface StreamProcessingFunction<A> extends IStreamedOperatorFunction {
	
	public StreamedDataSet apply(
			StreamedDataSet inputStream,
			A args
	);
	
}
