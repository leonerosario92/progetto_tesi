package query.execution.operator.mergeonbitsets;

import query.ImplementationProvider;
import query.execution.operator.MaterializationOperator;
import query.execution.operator.RelOperatorType;


public class MergeOnBitesetsOperator extends MaterializationOperator<MergeOnBitSetsFunction, MergeOnBitSetsArgs>{

	private static final RelOperatorType OPERATOR_TYPE = RelOperatorType.MERGE_ON_BITSETS;
	
	public MergeOnBitesetsOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new MergeOnBitSetsArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getMergeOnBitsetsImpl();
	}
}
