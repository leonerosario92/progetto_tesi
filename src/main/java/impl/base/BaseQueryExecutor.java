package impl.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import cache.IDataProvisioner;
import context.Context;
import dataset.IDataSet;
import dataset.IEntity;
import dataset.ILayoutManager;
import model.FieldDescriptor;
import query.QueryProvider;
import query.execution.AbstractQueryExecutor;
import query.execution.ExecutionPlanBlock;
import query.execution.ExecutionPlanItem;
import query.execution.IQueryExecutor;

public class BaseQueryExecutor extends AbstractQueryExecutor {
	
	private ExecutorService executor;
	
	public BaseQueryExecutor(Context context) {
		super(context);
		executor = new ForkJoinPool();
	}

	
	@Override
	public IDataSet executePlan(ExecutionPlanBlock plan) {
		
		List<ExecutionPlanItem> itemList = plan.getItemList();
		IDataSet inputDataSet = loadInputDataSet(plan.getReferencedFields());
		
		for(ExecutionPlanItem item : itemList) {
			IDataSet itemInputSet = inputDataSet.getSubset(item.getReferencedField());
			item.setInputDataSet(itemInputSet);
		}
		
		Set<IDataSet> partialResults = executeItems(itemList);
		IDataSet result = layoutManager.mergeDatasets(partialResults);
		return result;
	}
	
	
	private Set<IDataSet> executeItems(List<ExecutionPlanItem> itemList) {
		Set<IDataSet> partialResults = new HashSet<>();	
		try {
			partialResults =
			executor.invokeAll(itemList)
			.stream()
			.map(future -> {
			    try {
			        return future.get();
			    }
			    catch (Exception e) {
			        throw new IllegalStateException(e);
			    }
			})
			.collect(Collectors.toSet());
		} catch (InterruptedException e) {
			//TODO manage exception properly
			throw new RuntimeException(e);
		}
		return partialResults;
	}


	private IDataSet loadInputDataSet(List<FieldDescriptor> inputFields) {
		return dataProvisioner.loadDataSet(inputFields);
	}

}
