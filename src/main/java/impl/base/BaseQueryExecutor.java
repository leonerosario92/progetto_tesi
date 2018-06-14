package impl.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import dataset.IDataSet;
import dispatcher.MeasurementType;
import query.execution.QueryExecutor;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.OperatorGroup;
import query.execution.QueryExecutionException;

public class BaseQueryExecutor extends QueryExecutor {

	private ExecutorService executor;

	public BaseQueryExecutor() {
		super();
		executor = new ForkJoinPool();
	}

	
	@Override
	public IDataSet executePlan(ExecutionPlan plan) throws QueryExecutionException {
		OperatorGroup rootExecutable = plan.getRootExecutable();
		Supplier<IDataSet> result = rootExecutable.execOperators(this);
		return result.get();
	}


	@Override
	public IDataSet executePlan(ExecutionPlan plan, Query query, MeasurementType measurement) {
		OperatorGroup rootExecutable = plan.getRootExecutable();
		Supplier<IDataSet> result = rootExecutable.execOperators(this, measurement);
		return result.get();
	}
	
	
	
	@Override
	public Future<IDataSet> executeOperator(Callable<IDataSet> executable) {
		return executor.submit(executable);
	}



}
