package query.execution.operator.loadverticalpartition;

import query.ImplementationProvider;
import query.execution.operator.LoadDataSetOperator;
import query.execution.operator.RelOperatorType;

public class LoadVerticalPartitionOperator extends LoadDataSetOperator<LoadVerticalPartitionFunction,LoadVerticalPartitionArgs>{
	
	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_VERTICAL_PARTITION;
	
	public LoadVerticalPartitionOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new LoadVerticalPartitionArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadColumnSubsetImpl();
	}

}
