package query.execution;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import dataset.IDataSet;
import dispatcher.MeasurementType;
import objectexplorer.MemoryMeasurer;
import utils.ExecutionPlanNavigator;
import utils.report.IExecutionReport;
import utils.report.OperatorGroupReport;

public class SequentialOperatorGroup implements OperatorGroup {
	
	private LinkedList<ExecutionPlanElement> subElements;
	private OperatorGroupReport report; 
	private IDataSet inputDataSet;
	private LoadDataSetOperator dataLoader;
	private boolean generatesNewDataSet;
	

	public SequentialOperatorGroup() {
			this.report = new OperatorGroupReport();
			this.subElements = new LinkedList<>();
			this.generatesNewDataSet = false;
	}
	
	
	public SequentialOperatorGroup(IDataSet inputDataSet) {
		this();
		this.inputDataSet = inputDataSet;
	}
	
	
	public SequentialOperatorGroup(LoadDataSetOperator dataLoader) {
		this();
		this.dataLoader = dataLoader;
		this.generatesNewDataSet = true;
	}
	
	
	public void addSubElement(ExecutionPlanElement subElement, int position) {
		if(subElement.generatesNewDataSet()) {
			this.generatesNewDataSet = true;
		}
		subElements.add(position,subElement);
	}

	
	public void queueSubElement(ExecutionPlanElement subElement) {
		if(subElement.generatesNewDataSet()) {
			this.generatesNewDataSet = true;
		}
		subElements.addLast(subElement);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException {
		return execSubOperators(executor,MeasurementType.NONE);
	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		Callable<IDataSet> callable = getCallable(executor,measurement);
		
		IDataSet result = executor.submit(callable).getResult();
//		IResultHolder<IDataSet> result = result = executor.submit(callable);
		for(ExecutionPlanElement subElement : subElements) {
			if(subElement.generatesNewDataSet()) {
				this.report.sumMemoryOccupationMByte(
						subElement.getReport().getMemoryOccupationMB());
			}
		}
		
		return new IResultHolder<IDataSet>() {
			@Override
			public IDataSet getResult() {
				return result;
			}
		};
//		return result;
	}
		
		
	private Callable<IDataSet> getCallable(IQueryExecutor executor, MeasurementType measurement) {
		return new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				if(dataLoader != null) {
					report.setDataLoadingStartTIme();
					inputDataSet = dataLoader.execOperator(executor,measurement);
					report.setDataLoadingEndTIme();
					report.sumMemoryOccupationMByte(
							dataLoader.getReport().getMemoryOccupationMB());
				}
				
				IDataSet result = execOperatorSequence(executor,measurement);
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
				currentDataSet = ((Operator) nextOperator).execOperator(executor,measurement);
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
		public IExecutionReport getReport() {
			return report;
		}


		@Override
		public boolean generatesNewDataSet() {
			return this.generatesNewDataSet;
		}
	
}
