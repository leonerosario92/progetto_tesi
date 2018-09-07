package query.execution.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.execution.IReportableExecutable;
import query.execution.IQueryExecutor;
import query.execution.IResultHolder;
import query.execution.QueryExecutionException;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorGroupReport;
import utils.report.ReportAggregator;

public class ParallelOperatorGroup implements IOperatorGroup{
	
	private List<IOperatorGroup> subElements;
	private MaterializationOperator<?,?> materializationOperator;
	private boolean generatesNewDataSet;
	
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	
	
	public ParallelOperatorGroup(MaterializationOperator<?,?> materializationOperator) {
//		this.report = new OperatorGroupReport();
		this.subElements = new ArrayList<>();
		this.materializationOperator = materializationOperator;
		this.generatesNewDataSet = false;
		
		executionStartTime = executionEndTime = 0;
		
		executed = false;
	}
	
	
	public void addSubElement(IOperatorGroup subElement) {
		if(subElement.increaseMemoryOccupation()) {
			this.generatesNewDataSet = true;
		}
		subElements.add(subElement);
	}
	
	
	@Override
	public IDataSet execute(IQueryExecutor executor) {
		return execute(executor, MeasurementType.NONE);
	}

	
	@Override
	public IDataSet execute(IQueryExecutor executor, MeasurementType measurement) {
		List<IDataSet> partialResults = getPartialResults (executor, measurement);
		
		setExecutionStartTime();
		
		List<IDataSet> partialResultList = partialResults.stream()
				.collect(Collectors.toList());	
		
		IDataSet result = executor
				.submit(getMaterializationCallable(partialResultList,executor,measurement))
				.getResult();
		
		setExecutionEndTime();
		executed = true;
		return result;
	}


	private List<IDataSet> getPartialResults(IQueryExecutor executor, MeasurementType measurement) {
		List<IDataSet> partialResults = new ArrayList<>();
		for(IOperatorGroup operator : subElements) {
			try {
				
				partialResults.add(operator.execute(executor,measurement));
			} catch (QueryExecutionException e) {
				//TODO Manage exception
				e.printStackTrace();
			}
		}
		return partialResults;
	}
	

	private Callable<IDataSet> getMaterializationCallable(
			List<IDataSet> partialResultList,
			IQueryExecutor executor,
			MeasurementType measurement)
	{
		IDataSet[] inputDataSets = partialResultList.toArray(new IDataSet[partialResultList.size()]);
		materializationOperator.setInputData(inputDataSets);
		Callable<IDataSet> result = 
			new Callable<IDataSet>() {
				@Override
				public IDataSet call() throws Exception {
					return materializationOperator.execute(executor,measurement);
				}
			};
		return result;
	}
	
	
	private void setExecutionStartTime() {
		this.executionStartTime = System.nanoTime();
	}

	private void setExecutionEndTime() {
		this.executionEndTime = System.nanoTime();
	}

	private float getExecutionTimeMillis() {
		if (!this.executed) {
			// TODO Manage exception properly
			throw new IllegalStateException("Attempt to retrieve execution Time from a non-executed operator group");
		}
		return (new Float((executionEndTime - executionStartTime) / (1000 * 1000)));
	}
	
	
	@Override
	public void addRepresentation(ExecutableTreeNavigator printer) {
		printer.appendLine("[PARALLEL GROUP]");
		
		for( IReportableExecutable e : subElements) {
			printer.addIndentation();
			e.addRepresentation(printer);
			printer.removeIndentation();
		}
		
		printer.addIndentation();
		materializationOperator.addRepresentation(printer);
		printer.removeIndentation();
		
		printer.appendLine("[END PARALLEL GROUP]");
	}

	
	@Override
	public void addExecutionReport(ExecutableTreeNavigator printer) {
		
		if(!this.executed) {
			throw new IllegalStateException("Attempt to retrieve execution report from non-executed operator group");
		}
		
		printer.appendLine("[PARALLEL GROUP " + getReport().toString() +  "]");
		
		for( IReportableExecutable e : subElements) {
			printer.addIndentation();
			e.addExecutionReport(printer);
			printer.removeIndentation();
		}
		
		printer.addIndentation();
		materializationOperator.addExecutionReport(printer);
		printer.removeIndentation();
//		
//		report.addRepresentation(printer);
		printer.appendLine("[END PARALLEL GROUP]");
	}
	
	

	@Override
	public IExecutionReport getReport() {
		
		if(!this.executed) {
			throw new IllegalStateException("Attempt to retrieve execution report from non-executed operator group");
		}
		
		ReportAggregator result = new ReportAggregator();
		result.sumMemoryOccupationMByte(materializationOperator.getReport().getMemoryOccupationMB());
		for(IOperatorGroup op : subElements) {
			if(op.increaseMemoryOccupation()) {
				result.sumMemoryOccupationMByte(
						op.getReport().getMemoryOccupationMB());
			}
		}
		return result;
	}


	@Override
	public boolean increaseMemoryOccupation() {
		return generatesNewDataSet;
	}


}
