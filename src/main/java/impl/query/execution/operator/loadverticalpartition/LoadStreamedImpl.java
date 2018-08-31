package impl.query.execution.operator.loadverticalpartition;

import java.util.Set;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import model.FieldDescriptor;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionArgs;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionFunction;

public class LoadStreamedImpl extends LoadVerticalPartitionFunction{

	@Override
	public IDataSet apply(IDataProvisioner provisioner, LoadVerticalPartitionArgs args) throws DataSourceException {
		Set<FieldDescriptor> columns = args.getColumns();
		return provisioner.loadStreamedDataSet (columns);
	}
}
