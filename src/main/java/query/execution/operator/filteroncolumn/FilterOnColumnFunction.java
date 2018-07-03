package query.execution.operator.filteroncolumn;

import query.execution.operator.DataSetProcessingFunction;


public abstract class FilterOnColumnFunction implements DataSetProcessingFunction <FilterOnColumnArgs> {
	
	private FilterOnColumnArgs args;
	
	public FilterOnColumnFunction() {}
		
	public void setArgs (FilterOnColumnArgs args) {
		this.args =  args;
	}
	
}
