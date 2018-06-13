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
import query.QueryProvider;
import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import query.builder.statement.SelectionStatement;
import query.execution.ExecutionPlan;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.loadcolumn.LoadColumnArgs;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import query.execution.DataLoader;
import query.execution.DataProcessor;
import query.execution.ExecutableBlock;
import query.execution.ExecutableSequence;

public class BaseQueryPlanner extends QueryPlanner {
	
	public BaseQueryPlanner() {
		super();
	}

	/*
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
		
		ExecutableBlock rootExecutable = new ExecutableBlock();
		
		Set<FieldDescriptor> unfilteredFields = 
			Sets.difference(projectionClause.getReferencedFields(), filterClause.getReferencedFields());
		
		setProjectionOperators(rootExecutable, unfilteredFields);
		setFilterOperators(rootExecutable,filterClause);
		
		ExecutionPlan result = new ExecutionPlan(rootExecutable);
		
		return result;
		
	}
	
	
	
	private void setProjectionOperators(ExecutableBlock rootExecutable, Set<FieldDescriptor> unfilteredFields) {
		for(FieldDescriptor field : unfilteredFields) {
			ExecutableSequence exSequence = new ExecutableSequence();
			DataLoader loader = getDataSetLoader(field);
			exSequence.setDataLoader(loader);
			rootExecutable.addExecutable(exSequence);
		}
	}

	
	private void setFilterOperators(ExecutableBlock rootExecutable, FilterClause filterClause) {
		Map<FieldDescriptor, Set<FilterStatement>> groupedStatements =
				statementsByField(filterClause.getStatements());
		
		FilterOnColumnFunction filterFunction = queryProvider.getFilterOnColumnImpl();
		groupedStatements.entrySet().forEach(
				(pair)->{
					ExecutableSequence exSequence = new ExecutableSequence();
					
					DataLoader loader = getDataSetLoader(pair.getKey());
					exSequence.setDataLoader(loader);
					
					DataProcessor filterOperator = new DataProcessor();
					filterOperator.setFunction(filterFunction);
					FilterOnColumnArgs filterArgs = new FilterOnColumnArgs();
					filterArgs.setField(pair.getKey());
					filterArgs.setStatements(pair.getValue());
					filterOperator.setArgs(filterArgs);
					exSequence.addOperator(filterOperator, 0);
					
					rootExecutable.addExecutable(exSequence);
				});
	}

	
	private DataLoader getDataSetLoader(FieldDescriptor field) {
		
		LoadColumnFunction loadFunction = queryProvider.getLoadColumnImpl();
		
		DataLoader loader = new DataLoader();
		loader.setFunction(loadFunction);
		LoadColumnArgs loadArgs = new LoadColumnArgs();
		loadArgs.setColumn(field);
		loadArgs.setLoadingType(LoadingType.WHOLE_DATASET);
		loader.setArgs(loadArgs);
		return loader;
		
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
