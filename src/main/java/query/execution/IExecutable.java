package query.execution;

import java.util.function.Supplier;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import impl.query.execution.ExecutionException;

public interface IExecutable {

	public Supplier<IDataSet> exec(IQueryExecutor executor, IDataProvisioner provisioner) throws ExecutionException;
	
}
