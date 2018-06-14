package query.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import utils.TreePrinter;

public class ParallelOperatorGroup implements OperatorGroup,ExecutionPlanElement{
	
	private List<OperatorGroup> executables;
	
	public ParallelOperatorGroup() {
		this.executables = new ArrayList<>();
	}
	
	public void addExecutable(OperatorGroup executable) {
		executables.add(executable);
	}

	@Override
	public Supplier<IDataSet> execOperators(IQueryExecutor executor) {
		List<Supplier<IDataSet>> futures = new ArrayList<>();
		for(OperatorGroup executable : executables) {
			try {
				futures.add(executable.execOperators(executor));
			} catch (QueryExecutionException e) {
				
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

	@Override
	public void addRepresentation(TreePrinter printer) {
		printer.appendLine("[BLOCK]");
		for( ExecutionPlanElement e : executables) {
			printer.addIndentation();
			e.addRepresentation(printer);
			printer.removeIndentation();
		}
		printer.appendLine("[END BLOCK]");
	}

	
	@Override
	public void addRepresentationWithReport(TreePrinter printer) {
		// TODO Auto-generated method stub
		printer.appendLine("[BLOCK]");
		for( ExecutionPlanElement e : executables) {
			printer.addIndentation();
			e.addRepresentationWithReport(printer);
			printer.removeIndentation();
		}
		printer.appendLine("[END BLOCK]");
	}


}
