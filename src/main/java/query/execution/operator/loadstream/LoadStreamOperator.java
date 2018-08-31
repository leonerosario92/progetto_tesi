package query.execution.operator.loadstream;

import query.ImplementationProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.StreamLoadingOperator;

public class LoadStreamOperator extends StreamLoadingOperator<LoadStreamFunction, LoadStreamArgs> {
	
	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_STREAMED;
	
	public LoadStreamOperator(ImplementationProvider provider) {
		super (provider,OPERATOR_TYPE);
		this.args = new LoadStreamArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadStreamImpl();
	}

}
