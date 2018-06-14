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
import dispatcher.MeasurementType;
import impl.base.BaseQueryExecutor;
import utils.TreePrinter;
import utils.report.ReportAggregator;

public class SequentialOperatorGroup implements OperatorGroup{

	private List<ProcessDataSetOperator> operators;
	private LoadDataSetOperator dataLoader;
	
	
	
	public SequentialOperatorGroup() {
		this.operators = new ArrayList<>();
	}
	
	
	public void addOperator(ProcessDataSetOperator operator, int index) {
		operators.add(index,operator);
	}
	
	
	public void setDataLoader(LoadDataSetOperator dataLoader) {
		this.dataLoader = dataLoader;
	}
	
	
	@Override
	public Supplier<IDataSet> execOperators(BaseQueryExecutor baseQueryExecutor, MeasurementType measurement) {
		switch(measurement) {
		case EVALUATE_PERFORMANCE :
		case EVALUATE_MEMORY_OCCUPATION : 
		}
	}
	
	
	@Override
	public Supplier<IDataSet>execOperators(IQueryExecutor executor) {
		
		Future<IDataSet> future = executor.executeOperator(new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				IDataSet dataSet = dataLoader.loadDataSet (executor.getProvisioner());
				Iterator<ProcessDataSetOperator> it = operators.iterator();
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
					throw new RuntimeException(e.getMessage());
				}
			}
		};	
	}


	@Override
	public void addRepresentation(TreePrinter printer) {
		
		printer.appendLine("[SEQUENCE]");
		printer.addIndentation();
		dataLoader.addRepresentation(printer);
		
		for(ProcessDataSetOperator op : operators) {
			op.addRepresentation(printer);
		}
		printer.removeIndentation();
		printer.appendLine("[END SEQUENCE]");
		
	}


	@Override
	public void addRepresentationWithReport(TreePrinter printer) {
		
		ReportAggregator reportAggregator = new ReportAggregator();
		reportAggregator.sumToDataSetLoadingTime(dataLoader.getReport());
		
		for (ProcessDataSetOperator operator : operators) {
			reportAggregator.sumToExecutionTime(operator.getReport());
			reportAggregator.sumToMemoryOccupation(operator.getReport());
		}
		
	}

}
