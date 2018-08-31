package query.execution.operator;

import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import datasource.DataSourceException;

public interface StreamLoadingFunction<A> extends IStreamedOperatorFunction {

	public Stream<Object[]> apply(IDataProvisioner provisioner, A args) throws DataSourceException;
	
}
