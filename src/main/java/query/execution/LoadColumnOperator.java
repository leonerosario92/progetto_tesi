package query.execution;

import query.ImplementationProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.loadcolumn.LoadColumnArgs;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import utils.TreePrinter;

public class LoadColumnOperator extends LoadDataSetOperator<LoadColumnFunction, LoadColumnArgs>{
	
	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_COLUMN;
	
	public LoadColumnOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new LoadColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadColumnImpl();
	}

}
