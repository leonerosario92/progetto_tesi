package query.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import impl.query.execution.ExecutionException;

public class ExecutableBlock implements IExecutable{
	
	private List<IExecutable> executables;
	
	public ExecutableBlock() {
		this.executables = new ArrayList<>();
	}
	
	public void addExecutable(IExecutable executable) {
		executables.add(executable);
	}

	@Override
	public Supplier<IDataSet> exec(IQueryExecutor executor, IDataProvisioner provisioner) {
		List<Supplier<IDataSet>> futures = new ArrayList<>();
		for(IExecutable executable : executables) {
			try {
				futures.add(executable.exec(executor, provisioner));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<IDataSet> partialResults = futures.stream()
				.map(future->future.get())
				.collect(Collectors.toList());	
		
		IDataSet result = 
			executor.getlayoutManager().mergeDatasets(partialResults);
		
		return new Supplier<IDataSet>() {
			@Override
			public IDataSet get() {
				return result;
			}
		};
	}

}
