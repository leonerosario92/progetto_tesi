package query.execution.operator.streamedgroupby;

import query.execution.operator.RelOperatorType;
import query.execution.operator.StreamProcessingOperator;
import query.optimization.ImplementationProvider;

public class StreamedGroupByOperator 
extends StreamProcessingOperator<StreamedGroupByFunction, StreamedGroupByArgs>
{
	
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.STREAMED_ORDER_BY;

	public StreamedGroupByOperator (ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new StreamedGroupByArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getStreamedGroupByImpl();
	}

}
