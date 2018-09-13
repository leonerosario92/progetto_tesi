package query.execution.operator.filteroncolumn;

import query.execution.operator.ProcessDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.optimization.ImplementationProvider;

public class FilterOnColumnOperator extends ProcessDataSetOperator<FilterOnColumnFunction, FilterOnColumnArgs>{
	
	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.FILTER_ON_COLUMN;

	public FilterOnColumnOperator(ImplementationProvider provider) {
		super(provider,OPERATOR_TYPE);
		this.args = new FilterOnColumnArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getFilterOnColumnImpl();
	}
	
	
}
