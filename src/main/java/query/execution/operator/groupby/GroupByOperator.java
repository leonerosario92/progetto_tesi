package query.execution.operator.groupby;

import query.ImplementationProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.SortDataSetOperator;

public class GroupByOperator extends SortDataSetOperator <GroupByFunction, GroupByArgs> {

	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.GROUP_BY;
	
	public GroupByOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new GroupByArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getGroupByImpl();
	}
}
