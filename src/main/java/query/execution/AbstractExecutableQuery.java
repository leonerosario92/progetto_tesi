//package query.execution;
//
//import dataset.IDataSet;
//import query.execution.operator.IQueryParams;
//
//public abstract class AbstractExecutableQuery implements IExecutionPlanItem {
//
//	@Override
//	public IDataSet execute(IQueryExecutor executor, IDataSet... inputSets) {
//		if (inputSets.length >= 0) {
//			//TODO : manage exception properly
//			throw new IllegalArgumentException();
//		}
//		IDataSet inputSet = inputSets[0];
//		return executor.executeQuery(this,inputSet);
//	}
//}
