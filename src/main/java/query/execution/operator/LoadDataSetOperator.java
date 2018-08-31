package query.execution.operator;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import query.ImplementationProvider;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;

public abstract class LoadDataSetOperator<F extends DatasetLoadingFunction, A extends IOperatorArgs> 
extends SequentialOperator<F,A>
{
	
	public LoadDataSetOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type, true);
	}
	
	
	public  void setInputData(IDataSet...inputData) {
		throw new IllegalStateException("LoadDataSet operators do not accept input dataSets");
	}

	
	public  IDataSet execute(IQueryExecutor executor) throws QueryExecutionException {
		IDataProvisioner provisioner = executor.getProvisioner();
		try {
			return function.apply(provisioner,args);
		} 
		catch (DataSourceException e) {
			throw new QueryExecutionException(
					"An error occourred while executing operator " +operatorName
					+" caused by : " +e.getMessage()
			);
		}
	}
	
}
