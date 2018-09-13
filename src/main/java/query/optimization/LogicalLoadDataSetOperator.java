package query.optimization;

import query.execution.operator.LogicalOperatorType;

public class LogicalLoadDataSetOperator implements ILogicalOperator<LoadDataSetOperatorArgs>{
	
	private final static LogicalOperatorType OPERATOR_TYPE = LogicalOperatorType.LOAD_DATASET;
	private LoadDataSetOperatorArgs args;
	
	
	public LogicalLoadDataSetOperator() {
		this.args = new LoadDataSetOperatorArgs();
	}

	
	@Override
	public LogicalOperatorType getLogicalType() {
		return OPERATOR_TYPE;
	}


	@Override
	public LoadDataSetOperatorArgs getArgs() {
		return args;
	}

}
