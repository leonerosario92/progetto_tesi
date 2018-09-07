package impl.query.execution.operator.loadcolumn;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import model.FieldDescriptor;
import query.execution.operator.loadcolumn.LoadColumnArgs;
import query.execution.operator.loadcolumn.LoadColumnFunction;

public class LoadColumnImpl extends LoadColumnFunction{

	@Override
	public IDataSet apply(IDataProvisioner provisioner, LoadColumnArgs args) throws DataSourceException {
		return null;
//		FieldDescriptor field = args.getColumn();
//		 return provisioner.loadSingleColumnDataset(field);
	}

	

}
