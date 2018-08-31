package query.execution.operator;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import query.execution.IQueryExecutor;
import query.execution.IResultHolder;
import query.execution.QueryExecutionException;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;

public class StreamOperatorGroup implements IOperatorGroup<IDataSet> {
	
	private LinkedList<StreamOperator<?,?>> subElements;
	private StreamLoadingOperator<?,?> streamLoader;
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	
	
	public StreamOperatorGroup(StreamLoadingOperator streamLoader) {
		// this.report = new OperatorGroupReport();
		this.subElements = new LinkedList<>();
		executionStartTime = executionEndTime = 0;
		executed = false;
		this.streamLoader = streamLoader;
	}
	
	
	public void addSubElement(StreamOperator subElement) {
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
		Callable<IDataSet> callable = getCallable(executor, measurement);
		
		setExecutionStartTime();
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
				Stream<Object[]> recordStream = streamLoader.loadStream(provisioner);
				long count = recordStream.count();
				System.out.println("Count = " + count);
				
//				for(StreamOperator operator : subElements) {
//					recordStream = operator.addToPipeline(recordStream);
//				}
//				
//				List<Object[]> resultRecords = callTerminalOperation(recordStream);
//				IDataSet result = executor.getlayoutManager().buildMaterializedDataSet(columnSequence,recordStream);
//				return result;
				return null;
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
			// TODO Manage exception properly
			throw new IllegalStateException("Attempt to retrieve execution Time from a non-executed operator group");
		}
		return (new Float((executionEndTime - executionStartTime) / (1000*1000)));
	}


	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void addRepresentation(ExecutionPlanNavigator navigator) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addRepresentationWithReport(ExecutionPlanNavigator navigator) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean generatesNewDataSet() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public IExecutionReport getReport() {
		// TODO Auto-generated method stub
		return null;
	}






}
