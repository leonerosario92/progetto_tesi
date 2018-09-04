package impl.base;

import java.util.List;
import com.google.common.collect.Sets;

import model.AggregationDescriptor;
import query.QueryPlanner;
import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.GroupByClause;
import query.builder.clause.OrderByClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.FilterStatement;
import query.execution.ExecutionPlan;
import query.execution.operator.SequentialOperatorGroup;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByOperator;
import query.execution.operator.loadmaterialized.LoadMaterializedArgs;
import query.execution.operator.loadmaterialized.LoadMaterializedOperator;
import query.execution.operator.orderby.OrderByOperator;

public class BaseQueryPlanner extends QueryPlanner {
	
	
	public BaseQueryPlanner() {
		super();
	}

	
	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		SelectionClause selectionClause = query.getSelectionClause();
		ProjectionClause projectionClause = query.getProjectionClause();
		FilterClause filterClause = query.getFilterClause();
		GroupByClause groupByClause = query.getGroupByClause();
		OrderByClause orderByClause = query.getOrderByClause();
		
		LoadMaterializedOperator dataLoadingOperator = 
				new LoadMaterializedOperator(implementationProvider);
		LoadMaterializedArgs dataLoadingArgs = dataLoadingOperator.getArgs();
		dataLoadingArgs.setColumns(projectionClause.getReferencedFields());
		List<FilterStatement> filterStatements = filterClause.getStatements();
		if(! (filterStatements.isEmpty())) {
			dataLoadingArgs.setFilterStatements(Sets.newHashSet(filterStatements));
		}
		
//		MergeOnBitesetsOperator materializator = new MergeOnBitesetsOperator(implementationProvider);
//		ParallelOperatorGroup filterStatements = new ParallelOperatorGroup(materializator);
//		Set<FieldDescriptor> unfilteredFields = 
//			Sets.difference(projectionClause.getReferencedFields(), filterClause.getReferencedFields());
//		
//		setProjectionOperators(filterStatements, unfilteredFields);
//		setFilterOperators(filterStatements,filterClause);
		
		SequentialOperatorGroup rootExecutable = new SequentialOperatorGroup(dataLoadingOperator);
		
		if( ! (groupByClause.getGroupingSequence().isEmpty())) {
			GroupByOperator groupByOp = new GroupByOperator(implementationProvider);
			GroupByArgs gbArgs = groupByOp.getArgs();
			gbArgs.setGroupingSequence(groupByClause.getGroupingSequence());
			gbArgs.setProjectionSequence(projectionClause.getProjectionSequence());
			
			for(AggregationDescriptor aggrField : projectionClause.getAggregations()) {
				gbArgs.addAggregation(aggrField);
			}
			
			for(AggregateFilterStatement statement : groupByClause.getAggregateFilterStatements()) {
				gbArgs.addAggregateFilter(statement);
			}
			
			if( ! (orderByClause.getOrderingSequence().isEmpty())) {
				gbArgs.setOrderingSequence(orderByClause.getOrderingSequence());
			}
				
			rootExecutable.queueSubElement(groupByOp);
		}
		else if( ! (orderByClause.getOrderingSequence().isEmpty())) {
			OrderByOperator orderByOp = new OrderByOperator(implementationProvider);
			orderByOp.getArgs().setOrderingSequence(orderByClause.getOrderingSequence());
			rootExecutable.queueSubElement(orderByOp);
		}
		
		ExecutionPlan execPlan = new ExecutionPlan(rootExecutable);
		String plan = execPlan.toString();
		return execPlan;
	}
	
	
//	private void setProjectionOperators(ParallelOperatorGroup rootExecutable, Set<FieldDescriptor> unfilteredFields) {
//		for(FieldDescriptor field : unfilteredFields) {
//			LoadDataSetOperator loader = getDataSetLoader(field);
//			SequentialOperatorGroup exSequence = new SequentialOperatorGroup(loader);
//			rootExecutable.addSubElement(exSequence);
//		}
//	}

	
//	private void setFilterOperators(ParallelOperatorGroup rootExecutable, FilterClause filterClause) {
//		
//		List<RelatedOperators> groupedOperators = groupFilterOperators(filterClause);
//		groupedOperators.forEach(
//			group -> {
//				LoadDataSetOperator datasetLoader = getDataSetLoader(group.getFields());
//				SequentialOperatorGroup exSequence = new SequentialOperatorGroup (datasetLoader);
//				
//				if(group.getFields().size() > 1) {
//					FilterOnMultipleColumnOperator filterOperator = new FilterOnMultipleColumnOperator (implementationProvider);
//					FilterOnMultipleColumnArgs filterArgs = filterOperator.getArgs();
//					filterArgs.setFields(group.getFields());
//					filterArgs.setStatements(group.getStatements());
//					exSequence.queueSubElement(filterOperator);
//				}else if (group.getFields().size() == 1) {
//					FilterOnColumnOperator filterOperator = new FilterOnColumnOperator(implementationProvider);
//					FilterOnColumnArgs filterArgs = filterOperator.getArgs();
//					filterArgs.setField(group.getFields().iterator().next());
//					
//					
//					//TODO Quick fix. Write this better
//					//::::::::::::::::::::::::::::::::::
//					Set<FilterStatement> castedStatements = new HashSet<FilterStatement>();
//					for(CFNode statement : group.getStatements()) {
//						castedStatements.add((FilterStatement) statement);
//					}
//					//::::::::::::::::::::::::::::::::::
//					
//					
//					filterArgs.setStatements(castedStatements);
//					exSequence.queueSubElement(filterOperator);
//				}
//				
//				rootExecutable.addSubElement(exSequence);
//			}
//		);
//	}

	
//	private List<RelatedOperators> groupFilterOperators(FilterClause filterClause) {
//		LinkedList<RelatedOperators> initialGrouping = new LinkedList<>();
//		
//		for(CFilterStatement statement : filterClause.getComposedStatements()) {
//			RelatedOperators relOps = new RelatedOperators();
//			relOps.addStatement(statement);
//			initialGrouping.add(relOps);
//		}
//		
//		for(FilterStatement statement : filterClause.getStatements()) {
//			RelatedOperators relOps = new RelatedOperators();
//			relOps.addStatement(statement);
//			initialGrouping.add(relOps);
//		}
//		
//		List<RelatedOperators> res = new ArrayList<>();
//		while(!(initialGrouping.isEmpty())) {
//			boolean merged = false;
//			RelatedOperators currentGroup = initialGrouping.getFirst();
//			initialGrouping.removeFirst();
//			do 
//				{
//					ListIterator<RelatedOperators> lit = initialGrouping.listIterator();
//					merged = false;
//					while(lit.hasNext()) {
//						RelatedOperators relOps = lit.next();
//						if(currentGroup.overlaps(relOps)) {
//							currentGroup.mergeWith(relOps);
//							lit.remove();
//							merged = true;
//						}
//					}
//				}
//			while(merged);
//			res.add(currentGroup);
//		}
//		return res;
//	}

	
//	private LoadDataSetOperator getDataSetLoader(FieldDescriptor field) {
//		LoadColumnOperator loadOperator = new LoadColumnOperator(implementationProvider);
//		LoadColumnArgs loadArgs = loadOperator.getArgs();
//		loadArgs.setColumn(field);
//		loadArgs.setLoadingType(LoadingType.LOAD_DATASET);
//		return loadOperator;
//	}
//	
//	
//	private LoadDataSetOperator getDataSetLoader(Set<FieldDescriptor> fields) {
//		LoadVerticalPartitionOperator loadOperator = new LoadVerticalPartitionOperator(implementationProvider);
//		LoadVerticalPartitionArgs loadArgs = loadOperator.getArgs();
//		loadArgs.setColumns(fields);
//		loadArgs.setLoadingType(LoadingType.LOAD_DATASET);
//		return loadOperator;
//	}
//	
//	
//	private class RelatedOperators{
//		private Set<FieldDescriptor> fields;
//		private Set<CFNode> statements;
//		public RelatedOperators() {
//			fields = new HashSet<>();
//			statements = new HashSet<>();
//		}
//		
//		
//		public boolean containsField (FieldDescriptor field) {
//			return fields.contains(field);
//		}
//		
//		
//		public void addStatement (CFNode statement) {
//			statements.add(statement);
//			fields.addAll(statement.getReferencedFields());
//		}
//
//		
//		public boolean overlaps(RelatedOperators other) {
//			return ! (Sets.intersection(fields, other.fields).isEmpty());
//		}
//
//		
//		public void mergeWith(RelatedOperators other) {
//			for(CFNode statement : other.statements) {
//				this.addStatement(statement);
//			}
//		}
//		
//		
//		public Set<FieldDescriptor> getFields() {
//			return fields;
//		}
//		
//		
//		public Set<CFNode> getStatements (){
//			return statements;
//		}
//		
//	}
}
