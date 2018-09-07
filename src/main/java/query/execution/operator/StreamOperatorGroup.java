package query.execution.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dataprovisioner.IDataProvisioner;
import dataset.ColumnDescriptor;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dispatcher.MeasurementType;
import impl.base.StreamPipeline;
import impl.base.StreamedDataSet;
import model.AggregationDescriptor;
import model.FieldDescriptor;
import query.execution.IQueryExecutor;
import query.execution.QueryExecutionException;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.ReportAggregator;

public class StreamOperatorGroup implements IOperatorGroup<IDataSet> {
	
	private LinkedList<StreamProcessingOperator<?,?>> subElements;
	private StreamLoadingOperator<?,?> streamLoader;
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	
	
	public StreamOperatorGroup(StreamLoadingOperator<?,?> streamLoader) {
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
				ILayoutManager layoutManager = executor.getlayoutManager();
				
				StreamPipeline streamPipeline = streamLoader.loadStream(provisioner);
				
				for(StreamProcessingOperator<?,?> operator : subElements) {
					streamPipeline = operator.addOperationToPipeline(streamPipeline);
				}
		
				Iterator<Object[]> recordIterator = streamPipeline.getRecordStream().iterator();
				List<ColumnDescriptor> columnSequence = getColumnSequence(streamPipeline);
				
				IDataSet result = 
						new StreamedDataSet(columnSequence, recordIterator);		
				
				return result;
			}
		};
	}
	
	
	private List<ColumnDescriptor> getColumnSequence(StreamPipeline streamSource) {
		List<ColumnDescriptor> columnSequence = new ArrayList<>();
		for(int i=0; i<streamSource.getFieldsCount(); i++) {
			ColumnDescriptor currentColumn = streamSource.getColumnDescriptor(i);
			columnSequence.add(currentColumn);
		}
		return columnSequence;
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
	public boolean generatesNewDataSet() {
		// TODO Auto-generated method stub
		return false;
	}


}
