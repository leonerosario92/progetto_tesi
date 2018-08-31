package impl.query.execution.operator.loadstream;

import java.util.Set;
import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import model.FieldDescriptor;
import query.execution.operator.loadstream.LoadStreamArgs;
import query.execution.operator.loadstream.LoadStreamFunction;

public class LoadStreamImpl extends LoadStreamFunction {

	@Override
	public Stream<Object[]> apply(IDataProvisioner provisioner, LoadStreamArgs args) throws DataSourceException {
		Set<FieldDescriptor> columns = args.getColumns();
		IDataSet sourceDataSet = provisioner.loadStreamedDataSet(columns);
		return sourceDataSet.getRecordStream();
	}
	
}
