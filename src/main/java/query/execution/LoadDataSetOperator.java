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
		super(provider, type, true);
	}
	
	
	public  void setInputData(IDataSet...inputData) {
		throw new IllegalStateException("LoadDataSet operators do not accept input dataSets");
	}

	
	public  IDataSet execOperator(IQueryExecutor executor) throws QueryExecutionException {
		
		IDataProvisioner provisioner = executor.getProvisioner();
		try {
			return function.apply(provisioner,args);
		} catch (DataSourceException e) {
			throw new QueryExecutionException(
					"An error occourred while executing operator " +operatorName
					+" caused by : " +e.getMessage()
					);
		}
	}
	
}
