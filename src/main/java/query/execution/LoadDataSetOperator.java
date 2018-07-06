package query.execution;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import query.ImplementationProvider;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.DatasetLoadingFunction;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.RelOperatorType;

public abstract class LoadDataSetOperator<F extends DatasetLoadingFunction, A extends IOperatorArgs> extends Operator<F,A>{
	
	
	public LoadDataSetOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type);
	}

	
	public IDataSet loadDataSet (IDataProvisioner provisioner) throws DataSourceException {
		return (IDataSet) function.apply(provisioner,args);
	}
	
}
