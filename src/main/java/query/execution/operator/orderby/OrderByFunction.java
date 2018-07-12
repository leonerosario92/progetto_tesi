package query.execution.operator.orderby;

import dataset.IDataSet;
import query.execution.QueryExecutionException;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.MaterializationFunction;
import query.execution.operator.SortDataSetFunction;

public abstract class OrderByFunction implements SortDataSetFunction <OrderByArgs>{

	private OrderByArgs args;
	
	public OrderByFunction () {}
	
	public void setArgs (OrderByArgs args) {
		this.args = args;
	}

}
