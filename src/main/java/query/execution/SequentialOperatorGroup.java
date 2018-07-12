package query.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.DatasetLoadingFunction;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnOperator;
import utils.ExecutionPlanNavigator;
import utils.report.OperatorGroupReport;

public class SequentialOperatorGroup implements OperatorGroup {
	
	private LinkedList<ExecutionPlanElement> subElements;
	private OperatorGroupReport report; 
	private IDataSet inputDataSet;
	private LoadDataSetOperator dataLoader;
	

	public SequentialOperatorGroup() {
			this.report = new OperatorGroupReport();
			this.subElements = new LinkedList<>();
	}
	
	
	public SequentialOperatorGroup(IDataSet inputDataSet) {
		this();
		this.inputDataSet = inputDataSet;
	}
	
	
	public SequentialOperatorGroup(LoadDataSetOperator dataLoader) {
		this();
		this.dataLoader = dataLoader;
	}
	
	
	public void addSubElement(ExecutionPlanElement subElement, int position) {
		subElements.add(position,subElement);
	}

	
	public void queueSubElement(ExecutionPlanElement subElement) {
		subElements.addLast(subElement);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException {
		return execSubOperators(executor,MeasurementType.NONE);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		Callable<IDataSet> callable = getCallable(executor,measurement);
		IResultHolder<IDataSet> result = executor.submit(callable);
		return result;
	}
		
		
	private Callable<IDataSet> getCallable(IQueryExecutor executor, MeasurementType measurement) {
		return new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				if(dataLoader != null) {
					report.setDataLoadingStartTIme();
					inputDataSet = dataLoader.execOperator(executor);
					report.setDataLoadingEndTIme();
				}
				
				IDataSet result = execOperatorSequence(executor,measurement);
				
				if(measurement.equals(MeasurementType.EVALUATE_MEMORY_OCCUPATION)) {
					report.setMemoryOccupationByte(MemoryMeasurer.measureBytes(result));
				}
				return result;
			}
		};
	}

			
	private IDataSet execOperatorSequence(IQueryExecutor executor, MeasurementType measurement) throws QueryExecutionException {
				
		IDataSet currentDataSet = inputDataSet;
		ExecutionPlanElement  nextOperator;
		Iterator<ExecutionPlanElement> it = subElements.iterator();
		
		report.setExecutionStartTime();
		while(it.hasNext()) {
			nextOperator = it.next();
			if(nextOperator instanceof OperatorGroup) {
				currentDataSet = ((OperatorGroup)nextOperator)
						.execSubOperators(executor,measurement).getResult();
			}else if(nextOperator instanceof Operator) {
				((Operator)nextOperator).setInputData(currentDataSet);
				currentDataSet = ((Operator) nextOperator).execOperator(executor);
			}
		}
		report.setExecutionEndTime();
		return currentDataSet;
	}

		
		@Override
		public void addRepresentation(ExecutionPlanNavigator printer) {
			printer.appendLine("[SEQUENTIAL GROUP]");
			printer.addIndentation();
		
			for(ExecutionPlanElement op : subElements) {
				op.addRepresentation(printer);
			}
			
			printer.removeIndentation();
			printer.appendLine("[END SEQUENTIAL GROUP]");
		}

		
		@Override
		public void addRepresentationWithReport(ExecutionPlanNavigator printer) {
			printer.appendLine("[SEQUENTIAL GROUP]");
			printer.addIndentation();
		
			addOperatorsRepresentation(printer);
			report.addRepresentation(printer);
			
			printer.removeIndentation();
			printer.appendLine("[END SEQUENTIAL GROUP]");
		}
		
		
		private void addOperatorsRepresentation(ExecutionPlanNavigator printer) {
			
			if( ! (dataLoader == null)) {
				dataLoader.addRepresentationWithReport(printer);
			}
			
			for(ExecutionPlanElement op : subElements) {
				op.addRepresentationWithReport(printer);
			}
		}


		@Override
		public OperatorGroupReport getReport() {
			return report;
		}



		
}
