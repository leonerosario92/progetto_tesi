package query.execution.operator.filteronmultiplecolumn;

import query.execution.operator.DataSetProcessingFunction;

public abstract class FilterOnMultipleColumnFunction implements DataSetProcessingFunction <FilterOnMultipleColumnArgs> {
	
	private FilterOnMultipleColumnArgs args;
	
	public FilterOnMultipleColumnFunction() {}
		
	public void setArgs (FilterOnMultipleColumnArgs args) {
		this.args =  args;
	}

}
