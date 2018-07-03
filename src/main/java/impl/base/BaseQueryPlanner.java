package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
import query.builder.statement.CFNode;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import query.builder.statement.SelectionStatement;
import query.execution.ExecutionPlan;
import query.execution.operator.filteroncolumn.FilterOnColumnArgs;
import query.execution.operator.filteroncolumn.FilterOnColumnFunction;
import query.execution.operator.filteroncolumn.FilterOnColumnOperator;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnArgs;
import query.execution.operator.filteronmultiplecolumn.FilterOnMultipleColumnOperator;
import query.execution.operator.loadcolumn.LoadColumnArgs;
import query.execution.operator.loadcolumn.LoadColumnFunction;
import query.execution.operator.loadcolumn.LoadColumnOperator;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionArgs;
import query.execution.operator.loadverticalpartition.LoadVerticalPartitionOperator;
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
	 * all filter operation on a specific "fields group".
	 */
	
	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		SelectionClause selectionClause = query.getSelectionClause();
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
		
		List<RelatedOperators> groupedOperators = groupFilterOperators(filterClause);
		groupedOperators.forEach(
			group -> {
				LoadDataSetOperator datasetLoader = getDataSetLoader(group.getFields());
				SequentialOperatorGroup exSequence = new SequentialOperatorGroup (datasetLoader);
				
				if(group.getFields().size() > 1) {
					FilterOnMultipleColumnOperator filterOperator = new FilterOnMultipleColumnOperator (queryProvider);
					FilterOnMultipleColumnArgs filterArgs = filterOperator.getArgs();
					filterArgs.setFields(group.getFields());
					filterArgs.setStatements(group.getStatements());
					exSequence.queueOperator(filterOperator);
				}else if (group.getFields().size() == 1) {
					FilterOnColumnOperator filterOperator = new FilterOnColumnOperator(queryProvider);
					FilterOnColumnArgs filterArgs = filterOperator.getArgs();
					filterArgs.setField(group.getFields().iterator().next());
					
					
					//
					//::::::::::::::::::::::::::::::::::
					Set<FilterStatement> castedStatements = new HashSet<FilterStatement>();
					for(CFNode statement : group.getStatements()) {
						castedStatements.add((FilterStatement) statement);
					}
					//::::::::::::::::::::::::::::::::::
					
					
					filterArgs.setStatements(castedStatements);
					exSequence.queueOperator(filterOperator);
				}
				
				rootExecutable.addSubElement(exSequence);
			}
		);
	}

	
	private List<RelatedOperators> groupFilterOperators(FilterClause filterClause) {
		LinkedList<RelatedOperators> initialGrouping = new LinkedList<>();
		
		for(CFilterStatement statement : filterClause.getComposedStatements()) {
			RelatedOperators relOps = new RelatedOperators();
			relOps.addStatement(statement);
			initialGrouping.add(relOps);
		}
		
		for(FilterStatement statement : filterClause.getStatements()) {
			RelatedOperators relOps = new RelatedOperators();
			relOps.addStatement(statement);
			initialGrouping.add(relOps);
		}
		
		List<RelatedOperators> res = new ArrayList<>();
		while(!(initialGrouping.isEmpty())) {
			boolean merged = false;
			RelatedOperators currentGroup = initialGrouping.getFirst();
			initialGrouping.removeFirst();
			do 
				{
					ListIterator<RelatedOperators> lit = initialGrouping.listIterator();
					merged = false;
					while(lit.hasNext()) {
						RelatedOperators relOps = lit.next();
						if(currentGroup.overlaps(relOps)) {
							currentGroup.mergeWith(relOps);
							lit.remove();
							merged = true;
						}
					}
				}
			while(merged);
			res.add(currentGroup);
		}
		return res;
	}

	
	private LoadDataSetOperator getDataSetLoader(FieldDescriptor field) {
		LoadColumnOperator loadOperator = new LoadColumnOperator(queryProvider);
		LoadColumnArgs loadArgs = loadOperator.getArgs();
		loadArgs.setColumn(field);
		loadArgs.setLoadingType(LoadingType.LOAD_DATASET);
		return loadOperator;
	}
	
	
	private LoadDataSetOperator getDataSetLoader(Set<FieldDescriptor> fields) {
		LoadVerticalPartitionOperator loadOperator = new LoadVerticalPartitionOperator(queryProvider);
		LoadVerticalPartitionArgs loadArgs = loadOperator.getArgs();
		loadArgs.setColumns(fields);
		loadArgs.setLoadingType(LoadingType.LOAD_DATASET);
		return loadOperator;
	}
	
	
	private class RelatedOperators{
		private Set<FieldDescriptor> fields;
		private Set<CFNode> statements;
		public RelatedOperators() {
			fields = new HashSet<>();
			statements = new HashSet<>();
		}
		
		
		public boolean containsField (FieldDescriptor field) {
			return fields.contains(field);
		}
		
		
		public void addStatement (CFNode statement) {
			statements.add(statement);
			fields.addAll(statement.getReferencedFields());
		}

		
		public boolean overlaps(RelatedOperators other) {
			return ! (Sets.intersection(fields, other.fields).isEmpty());
		}

		
		public void mergeWith(RelatedOperators other) {
			for(CFNode statement : other.statements) {
				this.addStatement(statement);
			}
		}
		
		
		public Set<FieldDescriptor> getFields() {
			return fields;
		}
		
		
		public Set<CFNode> getStatements (){
			return statements;
		}
		
	}
}
