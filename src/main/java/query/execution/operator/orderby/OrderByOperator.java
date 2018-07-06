package query.execution.operator.orderby;

import query.ImplementationProvider;
import query.execution.ProcessDataSetOperator;
import query.execution.operator.RelOperatorType;

public class OrderByOperator extends ProcessDataSetOperator<OrderByFunction, OrderByArgs> {

	public static final RelOperatorType OPERATOR_TYPE = RelOperatorType.ORDER_BY;
	
	public OrderByOperator (ImplementationProvider provider) {
		super (provider, OPERATOR_TYPE);
		this.args = new OrderByArgs();
		this.operatorName = OPERATOR_TYPE.name();
		this.function = provider.getOrderByImpl();
	}

}
