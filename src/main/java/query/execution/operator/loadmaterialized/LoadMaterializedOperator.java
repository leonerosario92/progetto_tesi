package query.execution.operator.loadmaterialized;

import query.ImplementationProvider;
import query.execution.LoadDataSetOperator;
import query.execution.operator.RelOperatorType;

public class LoadMaterializedOperator extends LoadDataSetOperator<LoadMaterializedFunction, LoadMaterializedArgs> {

	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.LOAD_MATERIALIED;
	
	public LoadMaterializedOperator(ImplementationProvider provider) {
		super(provider, OPERATOR_TYPE);
		this.args = new LoadMaterializedArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getLoadMaterializedImpl();
	}

}
