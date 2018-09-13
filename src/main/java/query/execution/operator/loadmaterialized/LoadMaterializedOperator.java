package query.execution.operator.loadmaterialized;

import query.execution.operator.LoadDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.optimization.ImplementationProvider;

public class LoadMaterializedOperator extends LoadDataSetOperator<LoadMaterializedFunction, LoadMaterializedArgs> {

	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_MATERIALIED;
	
	public LoadMaterializedOperator(ImplementationProvider provider) {
		super(provider, OPERATOR_TYPE);
		this.args = new LoadMaterializedArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadMaterializedImpl();
	}

}
