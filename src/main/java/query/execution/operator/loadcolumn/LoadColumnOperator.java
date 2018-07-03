package query.execution.operator.loadcolumn;

import query.ImplementationProvider;
import query.execution.LoadDataSetOperator;
import query.execution.operator.RelOperatorType;
import utils.ExecutionPlanNavigator;

public class LoadColumnOperator extends LoadDataSetOperator<LoadColumnFunction, LoadColumnArgs>{
	
	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_COLUMN;
	
	public LoadColumnOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new LoadColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadColumnImpl();
	}

}
