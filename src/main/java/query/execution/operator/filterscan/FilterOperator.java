//package query.execution.operator.filterscan;
//
//import dataset.IDataSet;
//import query.execution.AbstractExecutableQuery;
//import query.execution.IExecutionPlanItem;
//import query.execution.operator.IQueryFunction;
//import query.execution.operator.IQueryParams;
//import query.execution.operator.IRelOperator;
//import query.execution.operator.RelOperatorType;
//
//public class FilterOperator implements IRelOperator {
//	
//	private static final  RelOperatorType type = RelOperatorType.FILTER;
//	private FilterScanFunction function;
//	
//	
//	private class FilterExecutableQuery extends AbstractExecutableQuery{
//		
//		FilterScanFunction function;
//		IDataSet inputSet;
//		IFilterQueryParams params;
//		
//		public FilterExecutableQuery(FilterScanFunction function) {
//			this.function = function;
//		}
//
//		@Override
//		public void setInputDataSet(IDataSet inputSet) {
//			this.inputSet = inputSet;
//		}
//
//		@Override
//		public void setParams(IQueryParams params) {
//			if(!(params instanceof IFilterQueryParams)) {
//				//TODO manage exception properly
//				throw new IllegalArgumentException();
//			}
//			
//			this.params = (IFilterQueryParams) params;
//		}
//
//		@Override
//		public IDataSet execQuery() {
//			return function.applyFunction(params, inputSet);
//		}
//	}
//	
//	
//	public FilterOperator(FilterScanFunction function) {
//		this.function = function;
//	}
//
//	@Override
//	public RelOperatorType getType() {
//		return type;
//	}
//
//	@Override
//	public IQueryFunction<?> getFunction() {
//		return function;
//	}
//
//	@Override
//	public IExecutionPlanItem getQuery() {
//		return new FilterExecutableQuery(function);
//	}
//}