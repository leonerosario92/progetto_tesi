package impl.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
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
		
		//TODO : fix this if selectStatements will accept more than one table
		TableDescriptor table = (TableDescriptor) plan.getReferencedTables().toArray()[0];
		IDataSet inputDataSet;
		try {
			inputDataSet = loadInputDataSet(table, plan.getReferencedFields());
			for(ExecutionPlanItem item : itemList) {
				IDataSet itemInputSet = inputDataSet.getSubset(item.getReferencedField());
				item.setInputDataSet(itemInputSet);
			}
			
			Set<IDataSet> partialResults = executeItems(itemList);
			IDataSet result = layoutManager.mergeDatasets(partialResults);
			return result;
		} catch (DataSourceException e) {
			throw new ExecutionException("An error occurred while executing query caused by : "+e.getMessage());
		}
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


	private IDataSet loadInputDataSet(TableDescriptor table , Set<FieldDescriptor> inputFields) throws DataSourceException {
			return dataProvisioner.loadDataSet(table , inputFields);
	}

}
