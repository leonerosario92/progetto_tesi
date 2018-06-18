package impl.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import query.execution.QueryExecutor;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.IResultHolder;
import query.execution.Operator;
import query.execution.OperatorGroup;
import query.execution.QueryExecutionException;

public class BaseQueryExecutor extends QueryExecutor {

	private ExecutorService executorService;

	public BaseQueryExecutor() {
		super();
		executorService = new ForkJoinPool();
	}

	
	@Override
	public IDataSet executePlan(ExecutionPlan plan) throws QueryExecutionException {
		OperatorGroup rootExecutable = plan.getRootExecutable();
		IResultHolder<IDataSet> result = rootExecutable.execSubOperators(this);
		return result.getResult();
	}


	@Override
	public IDataSet executePlan(ExecutionPlan plan, MeasurementType measurement) throws QueryExecutionException {
		OperatorGroup rootExecutable = plan.getRootExecutable();
		IResultHolder<IDataSet> result = rootExecutable.execSubOperators(this, measurement);
		return result.getResult();
	}
	
	
	
	@Override
	public IResultHolder <IDataSet> submit(Callable<IDataSet> executable) {
		Future<IDataSet> future = executorService.submit(executable);
		return new IResultHolder<IDataSet>() {

			@Override
			public IDataSet getResult() {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
		};
	}




}
