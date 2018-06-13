package impl.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import dispatcher.MeasurementType;
import model.FieldDescriptor;
import model.TableDescriptor;
import objectexplorer.MemoryMeasurer;
import query.execution.QueryExecutor;
import query.builder.Query;
import query.execution.ExecutionPlan;
import query.execution.IExecutable;
import query.execution.DataProcessor;
import query.execution.ExecutionException;

public class BaseQueryExecutor extends QueryExecutor {

	private ExecutorService executor;

	public BaseQueryExecutor() {
		super();
		executor = new ForkJoinPool();
	}

	@Override
	public IDataSet executePlan(ExecutionPlan plan) throws ExecutionException {
		IExecutable rootExecutable = plan.getRootExecutable();
		Supplier<IDataSet> result = rootExecutable.exec(this, getProvisioner());
		return result.get();
	}

	

	@Override
	public Future<IDataSet> execFunction(Callable<IDataSet> executable) {
		return executor.submit(executable);
	}
//		
//	@Override
//	public IDataSet executePlan(ExecutionPlan plan, Query query, MeasurementType measurement) throws ExecutionException {
//		switch (measurement) {
//		case EVALUATE_PERFORMANCE:
//			return execAndEvaluatePerformance(plan, query);
//		case EVALUATE_MEMORY_OCCUPATION:
//			return execAndEvaluateMemoryOccupation(plan,query);
//		default :
//			throw new IllegalArgumentException();
//		}
//	}
	

//	private IDataSet execAndEvaluatePerformance(ExecutionPlan plan, Query query) throws ExecutionException {
//		List<DataProcessor> itemList = plan.getItemList();
//		
//		query.setDataSetLoadingStartTime();
//		initializeItems(itemList);
//		query.setDataSetLoadingEndTime();
//		
//		query.setExecutionStartTime();
//		List<IDataSet> partialResults = executeItems(itemList);
//		IDataSet result = layoutManager.mergeDatasets(partialResults);
//		query.setExecutionEndTime();
//		
//		return result;
//	}
//	
//	
//	private IDataSet execAndEvaluateMemoryOccupation(ExecutionPlan plan, Query query) throws ExecutionException {
//		List<DataProcessor> itemList = plan.getItemList();	
//		initializeItems(itemList);
//		List<IDataSet> partialResults = executeItems(itemList);
//		IDataSet result = layoutManager.mergeDatasets(partialResults);
//		
//		query.setResultSetByteSize(MemoryMeasurer.measureBytes(result));
//		
//		return result;
//	}

	
//	private void initializeItems(List<DataProcessor> itemList) throws ExecutionException {
//		/*
//		 * For every execution item, load input DataSet
//		 */
//		for (DataProcessor item : itemList) {
//			List<FieldDescriptor> fields = item.getReferencedFields();
//
//			if (fields.size() > 1) {
//				// TODO Manage case when dataSet contains more than one column
//				throw new IllegalStateException();
//			}
//
//			FieldDescriptor field = fields.get(0);
//			try {
//				IDataSet inputSet = dataProvisioner.loadEntity(field);
//				item.setInputDataSet(inputSet);
//			} catch (DataSourceException e) {
//				throw new ExecutionException("Execution failed due to an error in loading input data"
//						+ System.getProperty("line.separator") + "caused by : " + e.getMessage());
//			}
//		}
//	}
	
	
//	private List<IDataSet> executeItems(List<DataProcessor> itemList) {
//		List<IDataSet> partialResults = new ArrayList<>();	
//		try {
//			partialResults =
//			executor.invokeAll(itemList)
//			.stream()
//			.map(future -> {
//			    try {
//			        return future.get();
//			    }
//			    catch (Exception e) {
//			        throw new IllegalStateException(e);
//			    }
//			})
//			.collect(Collectors.toList());
//		} catch (InterruptedException e) {
//			//TODO manage exception properly
//			throw new RuntimeException(e);
//		}
//		return partialResults;
//	}


}
