package query.execution;

import query.QueryProvider;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;

public class FilterOnColumnOperator extends ProcessDataSetOperator<FilterOnColumnFunction, FilterOnColumnArgs>{
	
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.FILTER_ON_COLUMN;

	public FilterOnColumnOperator(QueryProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new FilterOnColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getFilterOnColumnImpl();
	}
	
	
}
