package query.execution.operator;

import query.ImplementationProvider;

public abstract class StreamOperator<F extends IStreamedOperatorFunction, A extends IOperatorArgs>
extends Operator<F,A> {
	
	public StreamOperator(ImplementationProvider provider, RelOperatorType type) {}
	
}
