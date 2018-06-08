package query.execution.operator.filterscan;

import java.util.BitSet;

import dataset.IDataSet;
import query.execution.operator.IOperatorFunction;


public abstract class FilterOnColumnFunction  implements IOperatorFunction<FilterOnColumnArgs, IDataSet> {
	
	private FilterOnColumnArgs args;
	
	public FilterOnColumnFunction() {}
		
	public void setArgs (FilterOnColumnArgs args) {
		this.args =  args;
	}
	
}
