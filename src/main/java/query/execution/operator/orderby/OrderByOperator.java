package query.execution.operator.orderby;

import query.execution.operator.MaterializationOperator;
import query.execution.operator.ProcessDataSetOperator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.SortDataSetOperator;
import query.optimization.ImplementationProvider;

public class OrderByOperator extends SortDataSetOperator <OrderByFunction, OrderByArgs> {

	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.ORDER_BY;
	
	public OrderByOperator (ImplementationProvider provider) {
		super (provider, OPERATOR_TYPE);
		this.args = new OrderByArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getOrderByImpl();
	}

}
