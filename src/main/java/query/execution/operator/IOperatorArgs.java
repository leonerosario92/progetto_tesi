package query.execution.operator;

import dataset.IDataSet;

public interface IOperatorArgs {
	
	public void setInputDataSet(IDataSet inputSet);
	
	public IDataSet getInputDataSet();
	
}
