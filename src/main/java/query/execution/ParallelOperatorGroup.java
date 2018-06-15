package query.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import impl.base.BaseQueryExecutor;
import utils.IResultHolder;
import utils.TreePrinter;

public class ParallelOperatorGroup implements OperatorGroup,ExecutionPlanElement{
	
	private List<OperatorGroup> subElements;
	
	public ParallelOperatorGroup() {
		this.subElements = new ArrayList<>();
	}
	
	public void addExecutable(OperatorGroup executable) {
		subElements.add(executable);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) {
		
		List<IResultHolder<IDataSet>> partialResults = getPartialResults(executor);
		IResultHolder<IDataSet> result = mergePartialResults(partialResults,executor);
		return result;
		
	}

	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		List<IResultHolder<IDataSet>> partialResults = getPartialResults (executor, measurement);
		IResultHolder<IDataSet> result = mergePartialResults(partialResults,executor);
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
	

	
	private  IResultHolder<IDataSet> mergePartialResults(List<IResultHolder<IDataSet>> partialResults,IQueryExecutor executor) {
		List<IDataSet> partialResultList = partialResults.stream()
				.map(datasetHolder->datasetHolder.getResult())
				.collect(Collectors.toList());	
		
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
	public void addRepresentation(TreePrinter printer) {
		printer.appendLine("[BLOCK]");
		for( ExecutionPlanElement e : subElements) {
			printer.addIndentation();
			e.addRepresentation(printer);
			printer.removeIndentation();
		}
		printer.appendLine("[END BLOCK]");
	}

	
	@Override
	public void addRepresentationWithReport(TreePrinter printer) {
		// TODO Auto-generated method stub
		printer.appendLine("[BLOCK]");
		for( ExecutionPlanElement e : subElements) {
			printer.addIndentation();
			e.addRepresentationWithReport(printer);
			printer.removeIndentation();
		}
		printer.appendLine("[END BLOCK]");
	}


}
