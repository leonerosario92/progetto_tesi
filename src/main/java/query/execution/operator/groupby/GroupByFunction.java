package query.execution.operator.groupby;

import query.execution.operator.SortDataSetFunction;

public abstract class GroupByFunction implements SortDataSetFunction<GroupByArgs>{

	private GroupByArgs args;
	
	public GroupByFunction () {}
	
	public void setArgs(GroupByArgs args) {
		this.args = args;
	}
}
