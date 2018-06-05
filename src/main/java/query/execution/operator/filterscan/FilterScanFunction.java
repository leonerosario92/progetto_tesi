package query.execution.operator.filterscan;

import dataset.IDataSet;
import query.execution.operator.IOperatorFunction;


public abstract class FilterScanFunction  implements IOperatorFunction<FilterScanArgs, IDataSet> {
	
	private FilterScanArgs args;
	
	public FilterScanFunction() {}
		
	public void setArgs (FilterScanArgs args) {
		this.args =  args;
	}
	
}
