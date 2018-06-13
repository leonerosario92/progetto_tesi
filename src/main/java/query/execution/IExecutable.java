package query.execution;

import java.util.function.Supplier;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import utils.TreePrinter;

public interface IExecutable {


	public Supplier<IDataSet> exec(IQueryExecutor executor, IDataProvisioner provisioner) throws ExecutionException;
		
	
	public void addRepresentation (TreePrinter printer);
	
}
