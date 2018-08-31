package query.execution.operator;

import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import datasource.DataSourceException;
import query.ImplementationProvider;
import query.execution.QueryExecutionException;

public abstract class StreamLoadingOperator<F extends StreamLoadingFunction, A extends IOperatorArgs> 
extends StreamOperator<F,A>
{
	
	public StreamLoadingOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider,type);
	}
	
	public Stream<Object[]> loadStream (IDataProvisioner provisioner) throws QueryExecutionException{
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