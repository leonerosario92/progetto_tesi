package query.optimization;

import query.execution.operator.IOperatorArgs;
import query.execution.operator.LogicalOperatorType;


public interface ILogicalOperator<A extends IOperatorArgs>  {
	
	public LogicalOperatorType getLogicalType();
	
	public A getArgs();
	
}
