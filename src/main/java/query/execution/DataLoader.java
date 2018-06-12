package query.execution;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import query.execution.operator.DatasetLoadingFunction;
import query.execution.operator.IOperatorArgs;

public class DataLoader {

	private DatasetLoadingFunction function;
	private IOperatorArgs args;
	
	
	public void setFunction(DatasetLoadingFunction function) {
		this.function = function;
	}

	public void setArgs(IOperatorArgs args) {
		this.args = args;
	}
	
	public IDataSet loadDataSet (IDataProvisioner provisioner) throws DataSourceException {
		return (IDataSet) function.apply(provisioner,args);
	}
	
}
