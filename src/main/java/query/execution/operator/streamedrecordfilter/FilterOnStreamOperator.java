package query.execution.operator.streamedrecordfilter;

import query.ImplementationProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.StreamProcessingOperator;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnArgs;

public class FilterOnStreamOperator 
extends StreamProcessingOperator<FilterOnStreamFunction, FilterOnStreamArgs>
{
	
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.STREAMED_FILTER;
	
	public FilterOnStreamOperator (ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new FilterOnStreamArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getFilterOnStreamImpl();
	}
}
