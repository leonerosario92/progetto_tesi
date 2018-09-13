package query.execution.operator;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import impl.base.StreamedDataSet;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.ReportAggregator;

public class StreamedOperatorGroup implements IOperatorGroup {
	
	private LinkedList<StreamProcessingOperator<?,?>> subElements;
	private StreamLoadingOperator<?,?> streamLoader;
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	
	
	public StreamedOperatorGroup(StreamLoadingOperator<?,?> streamLoader) {
		this.subElements = new LinkedList<>();
		executionStartTime = executionEndTime = 0;
		executed = false;
		this.streamLoader = streamLoader;
	}
	
	
	public void addSubElement(StreamProcessingOperator<?,?> subElement) {
		subElements.add(subElement);
	}


	public IDataSet execute(IQueryExecutor executor) throws QueryExecutionException {
		return execute(executor, MeasurementType.NONE);
	}

	
	public IDataSet execute(IQueryExecutor executor, MeasurementType measurement) 
	throws QueryExecutionException
	{
		if (this.executed) {
			// TODO Manage exception properly
			throw new IllegalStateException("Attempt to execute operator group multiple times");
		}
		setExecutionStartTime();
		Callable<IDataSet> callable = getCallable(executor, measurement);
		IDataSet result = executor.submit(callable).getResult();
		setExecutionEndTime();
		this.executed = true;
		
		return result;
	}
	
	
	private Callable<IDataSet> getCallable(IQueryExecutor executor, MeasurementType measurement) {
		return new Callable<IDataSet>() {
			@Override
			public IDataSet call() throws Exception {
				
				IDataProvisioner provisioner = executor.getProvisioner();
				
				StreamedDataSet streamPipeline = streamLoader.loadStream(provisioner);
				
				for(StreamProcessingOperator<?,?> operator : subElements) {
					streamPipeline = operator.addOperationToPipeline(streamPipeline);
				}
		
				return streamPipeline;
			}
		};
	}
	
	
	private void setExecutionStartTime() {
		this.executionStartTime = System.nanoTime();
	}

	
	private void setExecutionEndTime() {
		this.executionEndTime = System.nanoTime();
	}

	
	private float getExecutionTimeMillis() {
		if (!this.executed) {
			throw new IllegalStateException("Attempt to retrieve execution Time from a non-executed operator group");
		}
		return (new Float((executionEndTime - executionStartTime) / (1000*1000)));
	}
	
	
	@Override
	public IExecutionReport getReport() {
		if(!this.executed) {
			throw new IllegalStateException("Attempt to retrieve execution report from non-executed operator group");
		}
		ReportAggregator result = new ReportAggregator();
		result.setExecutionTmeMs(getExecutionTimeMillis());
		return result;
	}

	
	@Override
	public void addRepresentation(ExecutableTreeNavigator navigator) {
		// TODO Auto-generated method stub
	}


	@Override
	public void addExecutionReport(ExecutableTreeNavigator navigator) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean increaseMemoryOccupation() {
		// TODO Auto-generated method stub
		return false;
	}


}
