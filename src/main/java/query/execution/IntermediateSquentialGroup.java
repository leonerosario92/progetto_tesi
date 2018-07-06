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
import utils.ExecutionPlanNavigator;
import utils.report.ExecutionReport;

public class IntermediateSquentialGroup implements OperatorGroup {
	
	private List<ExecutionPlanElement> subElements;
	private ExecutionReport report; 
//	private IDataSet inputDataSet;

	public IntermediateSquentialGroup() {
			this.report = new ExecutionReport();
			this.subElements = new ArrayList<>();
	}
	
	public void addSubElement(ExecutionPlanElement subElement) {
		subElements.add(subElement);
	}
	
	
//	public void setInputDataSet(IDataSet inputDataSet) {
//		this.inputDataSet = inputDataSet;
//	}
	
	
	@Override
	public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor, MeasurementType measurement) {
		
		Callable<IDataSet> callable = null;
		switch(measurement) {
		case NONE: 
			callable = getCallable (executor);
			break;
		case EVALUATE_MEMORY_OCCUPATION : 
			callable = getCallableMemEval(executor);
			break;
		case EVALUATE_EXECUTION_TIME:
			callable = getCallableExTimeEval(executor);
			break;
		}
	
		IResultHolder<IDataSet> result = executor.submit(callable);
		return result;
	}
		
	
	private Callable<IDataSet> getCallable(IQueryExecutor executor) {
		return new Callable<IDataSet>() {			
			@Override
			public IDataSet call() throws Exception {
				
				ExecutionPlanElement  nextOperator;
				Iterator<ExecutionPlanElement> it = subElements.iterator();
				IDataSet dataSet = null;
				while(it.hasNext()) {
					nextOperator = it.next();
					if(nextOperator instanceof OperatorGroup) {
						dataSet = ((OperatorGroup)nextOperator).execSubOperators(executor).getResult();
					}else if(nextOperator instanceof ProcessDataSetOperator) {
						dataSet = ((ProcessDataSetOperator)nextOperator).processDataSet(dataSet);
					}
			
				}
				return dataSet;
			};
		};
	}
		

		private Callable<IDataSet> getCallableMemEval(IQueryExecutor executor) {
			return new Callable<IDataSet>() {			
				@Override
				public IDataSet call() throws Exception {
					
					ExecutionPlanElement  nextOperator;
					Iterator<ExecutionPlanElement> it = subElements.iterator();
					IDataSet dataSet = null;
					
					while(it.hasNext()) {
						nextOperator = it.next();
						if(nextOperator instanceof OperatorGroup) {
							dataSet = ((OperatorGroup)nextOperator).execSubOperators(executor).getResult();
						}else if(nextOperator instanceof ProcessDataSetOperator) {
							dataSet = ((ProcessDataSetOperator)nextOperator).processDataSet(dataSet);
						}
					}
					report.setMemoryOccupationByte(MemoryMeasurer.measureBytes(dataSet));
					return dataSet;
				};
			};

		}
		
		
		private Callable<IDataSet> getCallableExTimeEval(IQueryExecutor executor) {
			return new Callable<IDataSet>() {			
				@Override
				public IDataSet call() throws Exception {
					
					ExecutionPlanElement  nextOperator;
					Iterator<ExecutionPlanElement> it = subElements.iterator();
					IDataSet dataSet = null;
					
					report.setExecutionStartTime();
					while(it.hasNext()) {
						nextOperator = it.next();
						if(nextOperator instanceof OperatorGroup) {
							dataSet = ((OperatorGroup)nextOperator).execSubOperators(executor).getResult();
						}else if(nextOperator instanceof ProcessDataSetOperator) {
							dataSet = ((ProcessDataSetOperator)nextOperator).processDataSet(dataSet);
						}
					}
					report.setExecutionEndTime();
					return dataSet;
				};
			};
		}

		@Override
		public void addRepresentation(ExecutionPlanNavigator printer) {
			printer.appendLine("[SEQUENCE]");
			printer.addIndentation();
		
			for(ExecutionPlanElement op : subElements) {
				op.addRepresentation(printer);
			}
			printer.removeIndentation();
			printer.appendLine("[END SEQUENCE]");
			
		}

		@Override
		public void addRepresentationWithReport(ExecutionPlanNavigator printer) {
			printer.appendLine("[SEQUENCE]");
			printer.addIndentation();
			printer.appendLine("[OPERATORS]");
			printer.addIndentation();
		
			for(ExecutionPlanElement op : subElements) {
				op.addRepresentation(printer);
			}
			printer.removeIndentation();
			printer.appendLine("[END OPERATORS]");
			
			report.addRepresentation(printer);
			printer.removeIndentation();
			printer.appendLine("[END SEQUENCE]");
		}

		@Override
		public IResultHolder<IDataSet> execSubOperators(IQueryExecutor executor) throws QueryExecutionException {
			Callable<IDataSet> callable = getCallable(executor);
			IResultHolder<IDataSet> result = executor.submit(callable);
			return result;
		}

		@Override
		public ExecutionReport getReport() {
			return report;
		}

		
}

