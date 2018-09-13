package query.execution.operator.filteronmultiplecolumn;

import query.execution.operator.ProcessDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.optimization.ImplementationProvider;

public class FilterOnMultipleColumnOperator 
extends ProcessDataSetOperator<FilterOnMultipleColumnFunction, FilterOnMultipleColumnArgs>
{
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.FILTER_ON_MULTIPLE_COLUMN;

	public FilterOnMultipleColumnOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new FilterOnMultipleColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getFilterOnMultipleColumnImpl();
	}
}
