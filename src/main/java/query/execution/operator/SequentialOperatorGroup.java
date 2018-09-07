package query.execution.operator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.naming.spi.DirStateFactory.Result;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import query.execution.IReportableExecutable;
import query.execution.IQueryExecutor;
import query.execution.IResultHolder;
import query.execution.QueryExecutionException;
import utils.ExecutableTreeNavigator;
import utils.report.IExecutionReport;
import utils.report.ReportAggregator;

public class SequentialOperatorGroup implements IOperatorGroup {

	private LinkedList<IReportableExecutable> subElements;
	private IDataSet inputDataSet;
	private LoadDataSetOperator<?,?> dataLoader;
	private boolean generatesNewDataSet;
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	

	public SequentialOperatorGroup() {
		this.subElements = new LinkedList<>();
		this.generatesNewDataSet = false;
		executionStartTime = executionEndTime = 0;
		executed = false;
	}

	
	public SequentialOperatorGroup(IDataSet inputDataSet) {
		this();
		this.inputDataSet = inputDataSet;
	}

	
	public SequentialOperatorGroup(LoadDataSetOperator<?, ?> dataLoader) {
		this();
		this.dataLoader = dataLoader;
		this.generatesNewDataSet = true;
	}

	
	public void addSubElement(IReportableExecutable subElement, int position) {
		if (subElement.increaseMemoryOccupation()) {
			this.generatesNewDataSet = true;
		}
		subElements.add(position, subElement);
	}

	
	public void queueSubElement(IReportableExecutable subElement) {
		if (subElement.increaseMemoryOccupation()) {
			this.generatesNewDataSet = true;
		}
		subElements.addLast(subElement);
	}

	
	@Override
	public IDataSet execute(IQueryExecutor executor) throws QueryExecutionException {
		return execute(executor, MeasurementType.NONE);
	}

	
	@Override
	public IDataSet execute(IQueryExecutor executor, MeasurementType measurement) {
		if (this.executed) {
			// TODO Manage exception properly
			throw new IllegalStateException("Attempt to execute operator group multiple times");
		}
		Callable<IDataSet> callable = getCallable(executor, measurement);
		IDataSet result = executor.submit(callable).getResult();
		this.executed = true;
		return result;
	}

	
	private Callable<IDataSet> getCallable(IQueryExecutor executor, MeasurementType measurement) {
		return new Callable<IDataSet>() {
			@Override
			public IDataSet call() throws Exception {

				setExecutionStartTime();
				if (dataLoader != null) {
					inputDataSet = dataLoader.execute(executor, measurement);
				}
				IDataSet result = execOperatorSequence(executor, measurement);
				setExecutionEndTime();
				
				return result;
			}
		};
	}

	
	private IDataSet execOperatorSequence(IQueryExecutor executor, MeasurementType measurement)
	throws QueryExecutionException 
	{
		IDataSet currentDataSet = inputDataSet;
		IReportableExecutable nextOperator;
		
		Iterator<IReportableExecutable> it = subElements.iterator();
		while (it.hasNext()) {
			nextOperator = it.next();
			if (nextOperator instanceof IOperatorGroup) {
				currentDataSet = 
					((IOperatorGroup) nextOperator).execute(executor, measurement);
			} else if (nextOperator instanceof SequentialOperator) {
				((SequentialOperator<?,?>) nextOperator).setInputData(currentDataSet);
				currentDataSet = 
					((SequentialOperator<?,?>) nextOperator).execute(executor, measurement);
			}
		}
		return currentDataSet;
	}

	
	@Override
	public void addRepresentation(ExecutableTreeNavigator printer) {
		printer.appendLine("[SEQUENTIAL GROUP]");
		printer.addIndentation();

		addOperatorsRepresentation(printer);

		printer.removeIndentation();
		printer.appendLine("[END SEQUENTIAL GROUP]");
	}

	
	@Override
	public void addExecutionReport(ExecutableTreeNavigator printer) {
		printer.appendLine("[SEQUENTIAL GROUP " + getReport().toString() + " ]");
		printer.addIndentation();

		addOperatorsRepresentationWithReport(printer);
		// report.addRepresentation(printer);

		printer.removeIndentation();
		printer.appendLine("[END SEQUENTIAL GROUP]");
	}

	
	private void addOperatorsRepresentationWithReport(ExecutableTreeNavigator printer) {
		if (!(dataLoader == null)) {
			dataLoader.addExecutionReport(printer);
		}

		for (IReportableExecutable op : subElements) {
			op.addExecutionReport(printer);
		}
	}
	
	
	private void addOperatorsRepresentation(ExecutableTreeNavigator printer) {

		if (!(dataLoader == null)) {
			dataLoader.addRepresentation(printer);
		}

		for (IReportableExecutable op : subElements) {
			op.addRepresentation(printer);
		}
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
		
		if(this.dataLoader != null) {
			result.sumMemoryOccupationMByte(dataLoader.getReport().getMemoryOccupationMB());
		}
		for (IReportableExecutable subElement : subElements) {
			if (subElement.increaseMemoryOccupation()) {
				result.sumMemoryOccupationMByte(subElement.getReport().getMemoryOccupationMB());
			}
		}
		
		return result;
	}

	
	@Override
	public boolean increaseMemoryOccupation() {
		return this.generatesNewDataSet;
	}

}
