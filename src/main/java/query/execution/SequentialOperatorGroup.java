package query.execution;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;

public class SequentialOperatorGroup implements OperatorGroup{

	private LinkedList<ProcessDataSetOperator> subElements;
	private LoadDataSetOperator dataLoader;
	private ExecutionReport report;
	
	
	public SequentialOperatorGroup(LoadDataSetOperator dataLoader ) {
		this.subElements = new LinkedList<>();
		this.dataLoader = dataLoader;
		this.report = new ExecutionReport();
	}
	
	
	public void addOperator(ProcessDataSetOperator operator, int index) {
		subElements.add(index,operator);
	}
	
	
	public void queueOperator(ProcessDataSetOperator operator) {
		subElements.addLast(operator);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException {
		return execSubOperators(executor,MeasurementType.NONE);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		
		Callable<IDataSet> callable = null;
		switch(measurement) {
		case NONE: 
			callable = getCallable (executor);
			break;
		case EVALUATE_MEMORY_OCCUPATION : 
			callable = getCallableMemEval(executor);
			break;
		case EVALUATE_EXECUTION_TIME:
			callable = getCallableExTimeEval(executor);
			break;
		}
	
		IResultHolder<IDataSet> result = executor.submit(callable);
		return result;
		
	}
	

	private Callable<IDataSet> getCallable(IQueryExecutor executor) {
		return 
				
			new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				IDataSet dataSet = dataLoader.execOperator(executor);
				
				ProcessDataSetOperator nextOperator;
				Iterator<ProcessDataSetOperator> it = subElements.iterator();
				while(it.hasNext()) {
					nextOperator = it.next();
					nextOperator.setInputData(dataSet);
					dataSet = nextOperator.execOperator(executor);
				}
				return dataSet;
			}
		};
	}
	

	private Callable<IDataSet> getCallableMemEval(IQueryExecutor executor) {
		return new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				IDataSet dataSet = dataLoader.execOperator(executor);
				report.setMemoryOccupationByte(MemoryMeasurer.measureBytes(dataSet));
				
				ProcessDataSetOperator nextOperator;
				Iterator<ProcessDataSetOperator> it = subElements.iterator();
				while(it.hasNext()) {
					nextOperator = it.next();
					nextOperator.setInputData(dataSet);
					dataSet = nextOperator.execOperator(executor);
				}
				
				return dataSet;
			}
		};
	}
	
	
	private Callable<IDataSet> getCallableExTimeEval(IQueryExecutor executor) {
		return new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				report.setDataLoadingStartTIme();
				IDataSet dataSet = dataLoader.execOperator(executor);
				report.setDataLoadingEndTIme();
				
				report.setExecutionStartTime();
				ProcessDataSetOperator nextOperator;
				Iterator<ProcessDataSetOperator> it = subElements.iterator();
				while(it.hasNext()) {
					nextOperator = it.next();
					nextOperator.setInputData(dataSet);
					dataSet = nextOperator.execOperator(executor);
				}
				report.setExecutionEndTime();
				
				return dataSet;
			}
		};
	}



	@Override
	public void addRepresentation(ExecutionPlanNavigator printer) {
		
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
	public void addRepresentationWithReport(ExecutionPlanNavigator printer) {
		
		printer.appendLine("[SEQUENCE]");
		printer.addIndentation();
		printer.appendLine("[OPERATORS]");
		printer.addIndentation();
		dataLoader.addRepresentation(printer);
		for(ProcessDataSetOperator op : subElements) {
			op.addRepresentation(printer);
		}
		printer.removeIndentation();
		printer.appendLine("[END OPERATORS]");
		
		report.addRepresentation(printer);
		printer.removeIndentation();
		printer.appendLine("[END SEQUENCE]");
	}


	@Override
	public ExecutionReport getReport() {
		return report;
	}


}
