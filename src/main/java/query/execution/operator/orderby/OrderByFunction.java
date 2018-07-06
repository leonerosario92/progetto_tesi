package query.execution.operator.orderby;

import dataset.IDataSet;
import query.execution.QueryExecutionException;
import query.execution.operator.DataSetProcessingFunction;

public abstract class OrderByFunction implements DataSetProcessingFunction<OrderByArgs>{

	private OrderByArgs args;
	
	public OrderByFunction () {}
	
	public void setArgs (OrderByArgs args) {
		this.args = args;
	}

}
