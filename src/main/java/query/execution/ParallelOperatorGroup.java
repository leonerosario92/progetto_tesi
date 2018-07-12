package query.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import utils.ExecutionPlanNavigator;
import utils.report.OperatorGroupReport;

public class ParallelOperatorGroup implements OperatorGroup,ExecutionPlanElement{
	
	private List<OperatorGroup> subElements;
	private OperatorGroupReport report;
	private MaterializationOperator<?,?> materializationOperator;
	
	public ParallelOperatorGroup(MaterializationOperator<?,?> materializationOperator) {
		this.report = new OperatorGroupReport();
		this.subElements = new ArrayList<>();
		this.materializationOperator = materializationOperator;
	}
	
	
	public void addSubElement(OperatorGroup subElement) {
		subElements.add(subElement);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) {
		return execSubOperators(executor, MeasurementType.NONE);
	}

	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		List<IResultHolder<IDataSet>> partialResults = getPartialResults (executor, measurement);
		
		report.setExecutionStartTime();
		List<IDataSet> partialResultList = partialResults.stream()
				.map(datasetHolder->datasetHolder.getResult())
				.collect(Collectors.toList());	
		report.setExecutionEndTime();

		report.setMaterializationStartTime();
		IDataSet result = executor
				.submit(getMaterializationCallable(partialResultList,executor))
				.getResult();
		report.setMaterializationEndTime();
		
		
		if (measurement == MeasurementType.EVALUATE_MEMORY_OCCUPATION) {
			float subOperatorsMemOccupation = 0;
			for(OperatorGroup op : subElements) {
				subOperatorsMemOccupation += op.getReport().getMemoryOccupationMB();
			}
			
			long materializationMemOccupation = 
					MemoryMeasurer.measureBytes(result);
			System.out.println("Materialization Occupation : " + materializationMemOccupation);
			System.out.println("SubOperators Occupation : " +subOperatorsMemOccupation);
			float totalMemOccupation = 
					(((float)materializationMemOccupation)/(1024*1024)) +
					subOperatorsMemOccupation;
			report.setMemoryOccupationMByte(totalMemOccupation);
		}
		
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
		printer.appendLine("[PARALLEL GROUP]");
		
		for( ExecutionPlanElement e : subElements) {
			printer.addIndentation();
			e.addRepresentationWithReport(printer);
			printer.removeIndentation();
		}
		
		printer.addIndentation();
		materializationOperator.addRepresentationWithReport(printer);
		printer.removeIndentation();
		
		report.addRepresentation(printer);
		printer.appendLine("[END PARALLEL GROUP]");
	}
	
	

	@Override
	public OperatorGroupReport getReport() {
		return report;
	}


}
