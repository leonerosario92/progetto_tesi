package query.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import utils.TreePrinter;

public class ExecutableSequence implements IExecutable{

	private List<DataProcessor> operators;
	private DataLoader dataLoader;
	
	
	
	private long datasetLoadingStartTime;
	private long datasetLoadingEndTime;
	private long executionStartTime;
	private long executionEndTime;
	private String executionStartDate;
	private String executionEndDate;
	private boolean executed;
	
	
	
	public ExecutableSequence() {
		this.operators = new ArrayList<>();
	}
	
	
	public void addOperator(DataProcessor operator, int index) {
		operators.add(index,operator);
	}
	
	
	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}
	
	
	@Override
	public Supplier<IDataSet>exec(IQueryExecutor executor, IDataProvisioner provisioner) {
		
		Future<IDataSet> future = executor.execFunction(new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				IDataSet dataSet = dataLoader.loadDataSet (provisioner);
				Iterator<DataProcessor> it = operators.iterator();
				while(it.hasNext()) {
					dataSet = it.next().processDataSet(dataSet);
				}
				return dataSet;
			}
		});
		
		return new Supplier<IDataSet> () {
			@Override
			public IDataSet get() {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					//TODO Manage exception properly
					throw new RuntimeException();
				}
			}
		};	
	}


	@Override
	public void addRepresentation(TreePrinter printer) {
		
		printer.appendLine("[SEQUENCE]");
		printer.addIndentation();
		printer.appendLine(dataLoader.toString());
		for(DataProcessor dp : operators) {
			printer.appendLine(dp.toString());
		}
		printer.removeIndentation();
		printer.appendLine("[END SEQUENCE]");
		
	}
	

}
