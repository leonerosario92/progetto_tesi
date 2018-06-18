package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import dataprovisioner.LoadingType;
import model.FieldDescriptor;
import query.QueryPlanner;
import query.ImplementationProvider;
import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import query.builder.statement.SelectionStatement;
import query.execution.ExecutionPlan;
import query.execution.FilterOnColumnOperator;
import query.execution.LoadColumnOperator;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.loadcolumn.LoadColumnArgs;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import query.execution.LoadDataSetOperator;
import query.execution.ProcessDataSetOperator;
import query.execution.ParallelOperatorGroup;
import query.execution.SequentialOperatorGroup;

public class BaseQueryPlanner extends QueryPlanner {
	
	public BaseQueryPlanner() {
		super();
	}

	/**
	 * Base implementation of a queryPlanner. Groups all filter statements 
	 * contained in the query by the field to which they will be applied
	 * and generates a query plan where every execution item is represented by 
	 * all filter operation on a specific field.
	 */
	
	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		//SelectionClause selectionClause = query.getSelectionClause();
		ProjectionClause projectionClause = query.getProjectionClause();
		FilterClause filterClause = query.getFilterClause();
		
		ParallelOperatorGroup rootExecutable = new ParallelOperatorGroup();
		
		Set<FieldDescriptor> unfilteredFields = 
			Sets.difference(projectionClause.getReferencedFields(), filterClause.getReferencedFields());
		
		setProjectionOperators(rootExecutable, unfilteredFields);
		setFilterOperators(rootExecutable,filterClause);
		
		ExecutionPlan result = new ExecutionPlan(rootExecutable);
		
		return result;
		
	}
	
	
	
	private void setProjectionOperators(ParallelOperatorGroup rootExecutable, Set<FieldDescriptor> unfilteredFields) {
		for(FieldDescriptor field : unfilteredFields) {
			LoadDataSetOperator loader = getDataSetLoader(field);
			SequentialOperatorGroup exSequence = new SequentialOperatorGroup(loader);
			rootExecutable.addSubElement(exSequence);
		}
	}

	
	private void setFilterOperators(ParallelOperatorGroup rootExecutable, FilterClause filterClause) {
		Map<FieldDescriptor, Set<FilterStatement>> groupedStatements =
				statementsByField(filterClause.getStatements());
		
		//FilterOnColumnFunction filterFunction = queryProvider.getFilterOnColumnImpl();
		groupedStatements.entrySet().forEach(
				(pair)->{
					LoadDataSetOperator datasetLoader = getDataSetLoader(pair.getKey());
					SequentialOperatorGroup exSequence = new SequentialOperatorGroup(datasetLoader);
					
					FilterOnColumnOperator filterOperator = new FilterOnColumnOperator(queryProvider);
					FilterOnColumnArgs filterArgs = filterOperator.getArgs();
					filterArgs.setField(pair.getKey());
					filterArgs.setStatements(pair.getValue());
					exSequence.addOperator(filterOperator, 0);
					
					rootExecutable.addSubElement(exSequence);
				});
	}

	
	private LoadDataSetOperator getDataSetLoader(FieldDescriptor field) {
		
		LoadColumnFunction loadFunction = queryProvider.getLoadColumnImpl();
		
		LoadColumnOperator loadOperator = new LoadColumnOperator(queryProvider);
		LoadColumnArgs loadArgs = loadOperator.getArgs();
		loadArgs.setColumn(field);
		loadArgs.setLoadingType(LoadingType.LOAD_DATASET);
		return loadOperator;
		
	}

	private Map<FieldDescriptor, Set<FilterStatement>> 
		statementsByField(List<FilterStatement> statements){
		HashMap<FieldDescriptor,Set<FilterStatement>> groupedStatements =
				new HashMap<> ();
		for(FilterStatement statement : statements) {
			FieldDescriptor field = statement.getField();
			if(groupedStatements.containsKey(field)) {
				groupedStatements.get(field).add(statement);
			}else {
				HashSet<FilterStatement> s = new HashSet<>();
				s.add(statement);
				groupedStatements.put(field, s);
			}
		}
		return groupedStatements;
	}
	
	
	
}
