package query.execution.operator.orderby;

import query.execution.operator.SortDataSetFunction;

public abstract class OrderByFunction implements SortDataSetFunction <OrderByArgs>{

	private OrderByArgs args;
	
	public OrderByFunction () {}
	
	public void setArgs (OrderByArgs args) {
		this.args = args;
	}

}
