package impl.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import datasource.DataSourceException;
import impl.query.execution.ExecutionException;
import model.FieldDescriptor;
import model.TableDescriptor;
import query.execution.QueryExecutor;
import query.execution.ExecutionPlan;
import query.execution.ExecutionPlanItem;

public class BaseQueryExecutor extends QueryExecutor {
	
	private ExecutorService executor;
	
	public BaseQueryExecutor() {
		super();
		executor = new ForkJoinPool();
	}
	
	
	@Override
	public IDataSet executePlan(ExecutionPlan plan) throws ExecutionException {
		List<ExecutionPlanItem> itemList = plan.getItemList();
		
		initializeItems(itemList);
		
		List<IDataSet> partialResults = executeItems(itemList);
		IDataSet result = layoutManager.mergeDatasets(partialResults);
		return result;
	}


	private void initializeItems(List<ExecutionPlanItem> itemList) throws ExecutionException {
		/*
		 * For every execution item, load input DataSet
		 */
		for(ExecutionPlanItem item : itemList) {
			List<FieldDescriptor> fields = item.getReferencedFields();
			
			if(fields.size() > 1) {
				//TODO Manage case when dataSet contains more than one column
				throw new IllegalStateException();
			}
			
			FieldDescriptor field = fields.get(0);
			try {
				IDataSet inputSet = dataProvisioner.loadEntity(field);
				item.setInputDataSet(inputSet);
			} catch (DataSourceException e) {
				throw new ExecutionException(
						"Execution failed due to an error in loading input data" +
						System.getProperty("line.separator") +
						"caused by : "+
						e.getMessage()
						);
			}
		}		
	}


	private List<IDataSet> executeItems(List<ExecutionPlanItem> itemList) {
//		List<IDataSet> partialResults = new ArrayList<>();	
		try {
			List<Future<IDataSet>>partialResults =
					executor.invokeAll(itemList);
			for(Future<IDataSet> partialResult : partialResults) {
				IDataSet ds = partialResult.get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		return null;
	}

}
