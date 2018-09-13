package query.execution.operator;

import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import datasource.DataSourceException;
import impl.base.StreamedDataSet;

public interface StreamLoadingFunction<A> extends IStreamedOperatorFunction {

	public StreamedDataSet apply(IDataProvisioner provisioner, A args) throws DataSourceException;
	
}
