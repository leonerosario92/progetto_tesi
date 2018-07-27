package impl.query.execution.operator.loadmaterialized;

import java.util.Set;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import model.FieldDescriptor;
import query.execution.operator.loadmaterialized.LoadMaterializedArgs;
import query.execution.operator.loadmaterialized.LoadMaterializedFunction;

public class LoadMaterializedImpl extends LoadMaterializedFunction{

	@Override
	public IDataSet apply(IDataProvisioner provisioner, LoadMaterializedArgs args) throws DataSourceException {
		Set<FieldDescriptor> columns = args.getColumns();
		return provisioner.loadMaterializedDataSet(columns);
	}

}
