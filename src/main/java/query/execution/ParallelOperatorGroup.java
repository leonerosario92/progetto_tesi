package query.execution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import impl.base.BaseQueryExecutor;
import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;

public class ParallelOperatorGroup implements OperatorGroup,ExecutionPlanElement{
	
	private List<OperatorGroup> subElements;
	private ExecutionReport report;
	
	public ParallelOperatorGroup() {
		this.report = new ExecutionReport();
		this.subElements = new ArrayList<>();
	}
	
	
	public void addSubElement(OperatorGroup subElement) {
		subElements.add(subElement);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) {
		List<IResultHolder<IDataSet>> partialResults = getPartialResults(executor);
		List<IDataSet> partialResultList = partialResults.stream()
				.map(datasetHolder->datasetHolder.getResult())
				.collect(Collectors.toList());	
		IResultHolder<IDataSet> result = mergePartialResults(partialResultList,executor);
		return result;
		
	}

	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		List<IResultHolder<IDataSet>> partialResults = getPartialResults (executor, measurement);
		
		report.setExecutionStartTime();
		List<IDataSet> partialResultList = partialResults.stream()
				.map(datasetHolder->datasetHolder.getResult())
				.collect(Collectors.toList());	
		
		IResultHolder<IDataSet> result = mergePartialResults(partialResultList,executor);
		report.setExecutionEndTime();
		
		if (measurement == MeasurementType.EVALUATE_MEMORY_OCCUPATION) {
			float totalMemoryOccupied = 0;
			for(OperatorGroup op : subElements) {
				totalMemoryOccupied += op.getReport().getMemoryOccupationMB();
			}
			report.setMemoryOccupationMByte(totalMemoryOccupied);
		}
		return result;
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
	
	
	private List<IResultHolder<IDataSet>> getPartialResults(IQueryExecutor executor) {
		List<IResultHolder<IDataSet>> partialResults = new ArrayList<>();
		for(OperatorGroup operator : subElements) {
			try {
				partialResults.add(operator.execSubOperators(executor));
			} catch (QueryExecutionException e) {
				//TODO Manage exception
				e.printStackTrace();
			}
		}
		return partialResults;
	}
	

	
	private  IResultHolder<IDataSet> mergePartialResults(List<IDataSet> partialResultList,IQueryExecutor executor) {
		
		IDataSet result = 
			executor.getlayoutManager().mergeDatasets(partialResultList);
		
		return new IResultHolder <IDataSet>() {
			@Override
			public IDataSet getResult() {
				return result;
			}
		};
	}

	

	@Override
	public void addRepresentation(ExecutionPlanNavigator printer) {
		printer.appendLine("[PARALLEL GROUP]");
		for( ExecutionPlanElement e : subElements) {
			printer.addIndentation();
			e.addRepresentation(printer);
			printer.removeIndentation();
		}
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
		report.addRepresentation(printer);
		printer.appendLine("[END PARALLEL GROUP]");
	}


	@Override
	public ExecutionReport getReport() {
		return report;
	}


}