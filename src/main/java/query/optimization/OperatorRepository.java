package query.optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import query.execution.operator.LogicalOperatorType;
import query.execution.operator.Operator;
import query.execution.operator.RelOperatorType;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnFunction;
import query.execution.operator.groupby.GroupByFunction;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import query.execution.operator.loadmaterialized.LoadMaterializedFunction;
import query.execution.operator.loadstream.LoadStreamFunction;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionFunction;
import query.execution.operator.mergeonbitsets.MergeOnBitSetsFunction;
import query.execution.operator.orderby.OrderByFunction;
import query.execution.operator.streamedgroupby.StreamedGroupByFunction;
import query.execution.operator.streamedorderby.StreamedOrderByFunction;
import query.execution.operator.streamedrecordfilter.FilterOnStreamFunction;

public class OperatorRepository  {
	
	private Table<ExecutionStrategy,LogicalOperatorType,Set<Operator<?,?>>> repository;
	 
	
	public OperatorRepository() {
		repository = HashBasedTable.create();
	}

	
	private boolean checkImplementation(ExecutionStrategy strategy,LogicalOperatorType logicalType) {
		if(repository.contains(strategy, logicalType)) {
			return true;
		}else {
			return false;
		}
	}
	
		
	public Set<Operator<?,?>> getImplementations(ExecutionStrategy strategy,LogicalOperatorType logicalType) {
		if(checkImplementation(strategy, logicalType)) {
			return repository.get(strategy, logicalType);
		}
		else {
			return Sets.newHashSet();
		}
	}
	
	
	private void addToRepository(Operator<?,?> operator) {
		ExecutionStrategy strategy = operator.getStrategy();
		LogicalOperatorType logicalType = operator.getLogicalType();
		repository.put(strategy, logicalType, operator);
	}


	public Set<ExecutionStrategy> getAvailableStrategies() {
		return repository.rowKeySet();
	}

}
