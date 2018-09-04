package query.execution.operator.streamedorderby;

import query.ImplementationProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.StreamProcessingOperator;
import query.execution.operator.streamedrecordfilter.FilterOnStreamArgs;
import query.execution.operator.streamedrecordfilter.FilterOnStreamFunction;

public class StreamedOrderByOperator 
extends StreamProcessingOperator<StreamedOrderByFunction, StreamedOrderByArgs>
{
	
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.STREAMED_ORDER_BY;

	public StreamedOrderByOperator (ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new StreamedOrderByArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getStreamedOrderByImpl();
	}
}
