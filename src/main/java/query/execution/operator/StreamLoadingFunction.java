package query.execution.operator;

import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import datasource.DataSourceException;
import impl.base.StreamPipeline;

public interface StreamLoadingFunction<A> extends IStreamedOperatorFunction {

	public StreamPipeline apply(IDataProvisioner provisioner, A args) throws DataSourceException;
	
}
