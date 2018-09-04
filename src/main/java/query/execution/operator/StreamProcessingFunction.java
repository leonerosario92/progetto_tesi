package query.execution.operator;

import java.util.Map;
import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import datasource.DataSourceException;

public interface StreamProcessingFunction<A> extends IStreamedOperatorFunction {
	
	public Stream<Object[]> apply(
			Stream<Object[]> inputStream,
			Map<String,Integer> nameIndexMapping,
			A args
	);
	
}
