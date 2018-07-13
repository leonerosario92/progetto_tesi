package query.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorGroupReport;
import utils.report.ReportAggregator;

public class ParallelOperatorGroup implements OperatorGroup,ExecutionPlanElement{
	
	private List<OperatorGroup> subElements;
//	private OperatorGroupReport report;
	private MaterializationOperator<?,?> materializationOperator;
	private boolean generatesNewDataSet;
	
	private boolean executed;
	private long executionStartTime;
	private long executionEndTime;
	private long materializedDatesetSize;
	
	
	public ParallelOperatorGroup(MaterializationOperator<?,?> materializationOperator) {
//		this.report = new OperatorGroupReport();
		this.subElements = new ArrayList<>();
		this.materializationOperator = materializationOperator;
		this.generatesNewDataSet = false;
		
		executionStartTime = executionEndTime = 0;
		materializedDatesetSize = 0;
		
		executed = false;
	}
	
	
	public void addSubElement(OperatorGroup subElement) {
		if(subElement.generatesNewDataSet()) {
			this.generatesNewDataSet = true;
		}
		subElements.add(subElement);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) {
		return execSubOperators(executor, MeasurementType.NONE);
	}

	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		List<IResultHolder<IDataSet>> partialResults = getPartialResults (executor, measurement);
		
		setExecutionStartTime();
		
//		report.setExecutionStartTime();
		List<IDataSet> partialResultList = partialResults.stream()
				.map(datasetHolder->datasetHolder.getResult())
				.collect(Collectors.toList());	
//		report.setExecutionEndTime();

//		report.setMaterializationStartTime();
		IDataSet result = executor
				.submit(getMaterializationCallable(partialResultList,executor))
				.getResult();
		
		if(measurement.equals(MeasurementType.EVALUATE_MEMORY_OCCUPATION)) {
			this.materializedDatesetSize = MemoryMeasurer.measureBytes(result);
		}
//		report.setMaterializationEndTime();
		
		
//		if (measurement == MeasurementType.EVALUATE_MEMORY_OCCUPATION) {
//			float subOperatorsMemOccupation = 0;
//			for(OperatorGroup op : subElements) {
//				subOperatorsMemOccupation += op.getReport().getMemoryOccupationMB();
//			}
//			report.setMemoryOccupationMByte(subOperatorsMemOccupation);
//			
//			long materializationMemOccupation = 
//					MemoryMeasurer.measureBytes(result);
//			report.sumMemoryOccupationByte(materializationMemOccupation);
//		}
		
		setExecutionEndTime();
		executed = true;
		
		return new IResultHolder<IDataSet>() {
			@Override
			public IDataSet getResult() {
				return result;
			}
		};
	}


	private List<IResultHolder<IDataSet>> getPartialResults(IQueryExecutor executor, MeasurementType measurement) {
		List<IResultHolder<IDataSet>> partialResults = new ArrayList<>();
		for(OperatorGroup operator : subElements) {
			try {
				partialResults.add(operator.execSubOperators(executor,measurement));
			} catch (QueryExecutionException e) {
				//TODO Manage exception
				e.printStackTrace();
			}
		}
		return partialResults;
	}
	

	private Callable<IDataSet> getMaterializationCallable(List<IDataSet> partialResultList, IQueryExecutor executor) {
		
		IDataSet[] inputDataSets = partialResultList.toArray(new IDataSet[partialResultList.size()]);
		materializationOperator.setInputData(inputDataSets);
		Callable<IDataSet> result = 
			new Callable<IDataSet>() {
				@Override
				public IDataSet call() throws Exception {
					return materializationOperator.execOperator(executor);
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
	public void addRepresentation(ExecutionPlanNavigator printer) {
		printer.appendLine("[PARALLEL GROUP]");
		
		for( ExecutionPlanElement e : subElements) {
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
	public void addRepresentationWithReport(ExecutionPlanNavigator printer) {
		
		if(!this.executed) {
			throw new IllegalStateException("Attempt to retrieve execution report from non-executed operator group");
		}
		
		printer.appendLine("[PARALLEL GROUP " + getReport().toString() +  "]");
		
		for( ExecutionPlanElement e : subElements) {
			printer.addIndentation();
			e.addRepresentationWithReport(printer);
			printer.removeIndentation();
		}
		
//		printer.addIndentation();
//		materializationOperator.addRepresentationWithReport(printer);
//		printer.removeIndentation();
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
		result.setMemoryOccupationByte(materializedDatesetSize);
		for(OperatorGroup op : subElements) {
			if(op.generatesNewDataSet()) {
				result.sumMemoryOccupationMByte(
						op.getReport().getMemoryOccupationMB());
			}
		}
		return result;
	}


	@Override
	public boolean generatesNewDataSet() {
		return generatesNewDataSet;
	}


}
