package query.execution.operator;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;

public interface DatasetLoadingFunction <A> extends ISequentialOperatorFunction {
	
	public IDataSet apply(IDataProvisioner provisioner, A args) throws DataSourceException;
	
}
 