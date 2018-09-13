package query.execution.operator.loadcolumn;

import query.execution.operator.LoadDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.optimization.ImplementationProvider;
import utils.ExecutableTreeNavigator;

public class LoadColumnOperator extends LoadDataSetOperator<LoadColumnFunction, LoadColumnArgs>{
	
	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_COLUMN;
	
	public LoadColumnOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new LoadColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadColumnImpl();
	}

}
