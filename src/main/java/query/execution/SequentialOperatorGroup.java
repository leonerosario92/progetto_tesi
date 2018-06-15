package query.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import utils.IResultHolder;
import utils.TreePrinter;
import utils.report.ReportAggregator;

public class SequentialOperatorGroup implements OperatorGroup{

	private List<ProcessDataSetOperator> subElements;
	private LoadDataSetOperator dataLoader;
	
	
	
	public SequentialOperatorGroup(LoadDataSetOperator dataLoader ) {
		this.subElements = new ArrayList<>();
		this.dataLoader = dataLoader;
	}
	
	
	public void addOperator(ProcessDataSetOperator operator, int index) {
		subElements.add(index,operator);
	}
	
	
//	public void setDataLoader(LoadDataSetOperator dataLoader) {
//		this.dataLoader = dataLoader;
//	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		
		IResultHolder<IDataSet> result = executor.submit(
				new Callable<IDataSet>() {			
					@Override
					public IDataSet call() throws Exception {
						IDataSet dataSet = dataLoader.loadDataSet (executor.getProvisioner());
						ProcessDataSetOperator nextOperator;
						Iterator<ProcessDataSetOperator> it = subElements.iterator();
						while(it.hasNext()) {
							nextOperator = it.next();
							dataSet = nextOperator.processDataSet(dataSet);
						}
						return dataSet;
					}
		});
		return result;
		
	}
	
	
//	@Override
//	public Supplier<IDataSet>execOperators(IQueryExecutor executor) {
//		
//		Future<IDataSet> future = executor.executeOperator(
//				new Callable<IDataSet>() {			
//					@Override
//					public IDataSet call() throws Exception {
//						IDataSet dataSet = dataLoader.loadDataSet (executor.getProvisioner());
//						Iterator<ProcessDataSetOperator> it = operators.iterator();
//						while(it.hasNext()) {
//							dataSet = it.next().processDataSet(dataSet);
//						}
//						return dataSet;
//					}
//		});
//		
//		return new Supplier<IDataSet> () {
//			@Override
//			public IDataSet get() {
//				try {
//					return future.get();
//				} catch (InterruptedException | ExecutionException e) {
//					//TODO Manage exception properly
//					throw new RuntimeException(e.getMessage());
//				}
//			}
//		};	
//	}


	@Override
	public void addRepresentation(TreePrinter printer) {
		
		printer.appendLine("[SEQUENCE]");
		printer.addIndentation();
		dataLoader.addRepresentation(printer);
		
		for(ProcessDataSetOperator op : subElements) {
			op.addRepresentation(printer);
		}
		printer.removeIndentation();
		printer.appendLine("[END SEQUENCE]");
		
	}


	@Override
	public void addRepresentationWithReport(TreePrinter printer) {
		
		ReportAggregator reportAggregator = new ReportAggregator();
		reportAggregator.sumToDataSetLoadingTime(dataLoader.getReport());
		
		for (ProcessDataSetOperator operator : subElements) {
			reportAggregator.sumToExecutionTime(operator.getReport());
			reportAggregator.sumToMemoryOccupation(operator.getReport());
		}
		
	}


	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException {
		return execSubOperators(executor,MeasurementType.NONE);
	}


}
