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
import query.execution.operator.Operator;
import query.execution.operator.IOperatorGroup;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.IResultHolder;
import query.execution.QueryExecutionException;

public class BaseQueryExecutor extends QueryExecutor {

	private ExecutorService executorService;

	public BaseQueryExecutor() {
		super();
		executorService = new ForkJoinPool();
	}

	
	@Override
	public IDataSet executePlan(ExecutionPlan plan) throws QueryExecutionException {
		IOperatorGroup rootExecutable = plan.getRootExecutable();
		IDataSet result =  rootExecutable.execute(this);
		return result;
	}


	@Override
	public IDataSet executePlan(ExecutionPlan plan, MeasurementType measurement) throws QueryExecutionException {
		IOperatorGroup rootExecutable = plan.getRootExecutable();
		IDataSet result = rootExecutable.execute(this, measurement);
		return result;
	}
	
	
	
	public <T> IResultHolder <T> submit(Callable<T> executable) {
		Future<T> future = executorService.submit(executable);
		return new IResultHolder<T>() {

			@Override
			public T getResult() {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					throw new RuntimeException("An error occourred during operator execution, caused by " + e.getMessage());
				}
			}
		};
	}




}
