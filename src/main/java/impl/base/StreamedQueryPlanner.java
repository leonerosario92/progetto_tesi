package impl.base;

import java.util.List;

import com.google.common.collect.Sets;

import model.AggregationDescriptor;
import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.GroupByClause;
import query.builder.clause.OrderByClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.FilterStatement;
import query.execution.ExecutionPlan;
import query.execution.operator.IOperatorGroup;
import query.execution.operator.StreamedOperatorGroup;
import query.execution.operator.groupby.GroupByArgs;
import query.execution.operator.groupby.GroupByOperator;
import query.execution.operator.loadstream.LoadStreamArgs;
import query.execution.operator.loadstream.LoadStreamOperator;
import query.execution.operator.orderby.OrderByOperator;
import query.execution.operator.streamedgroupby.StreamedGroupByArgs;
import query.execution.operator.streamedgroupby.StreamedGroupByOperator;
import query.execution.operator.streamedorderby.StreamedOrderByOperator;
import query.execution.operator.streamedrecordfilter.FilterOnStreamArgs;
import query.execution.operator.streamedrecordfilter.FilterOnStreamOperator;
import query.optimization.QueryPlanner;

public class StreamedQueryPlanner extends QueryPlanner{
	
	public StreamedQueryPlanner() {
		super();
	}

	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		query.getSelectionClause();
		ProjectionClause projectionClause = query.getProjectionClause();
		FilterClause filterClause = query.getFilterClause();
		GroupByClause  groupByClause = query.getGroupByClause();
		OrderByClause orderByClause = query.getOrderByClause();
		
		LoadStreamOperator dataLoadingOperator = 
			new LoadStreamOperator(implementationProvider);
		LoadStreamArgs dataLoadingArgs = dataLoadingOperator.getArgs();
		dataLoadingArgs.setColumns(projectionClause.getReferencedFields());
		
		StreamedOperatorGroup rootExecutable = new StreamedOperatorGroup(dataLoadingOperator);
		
		List<FilterStatement> filterStatements = filterClause.getStatements();
		if(! (filterStatements.isEmpty())) {
			FilterOnStreamOperator filterOperator = 
					new FilterOnStreamOperator(implementationProvider);
			FilterOnStreamArgs filterArgs = filterOperator.getArgs();
			filterArgs.setStatements(Sets.newHashSet(filterStatements));
			rootExecutable.addSubElement(filterOperator);
		}
		
		if( ! (groupByClause.getGroupingSequence().isEmpty())) {
			StreamedGroupByOperator groupByOp = new StreamedGroupByOperator(implementationProvider);
			StreamedGroupByArgs gbArgs = groupByOp.getArgs();
			gbArgs.setGroupingSequence(groupByClause.getGroupingSequence());
			gbArgs.setProjectionSequence(projectionClause.getProjectionSequence());
			for(AggregationDescriptor aggrField : projectionClause.getAggregations()) {
				gbArgs.addAggregation(aggrField);
			}
			for(AggregateFilterStatement statement : groupByClause.getAggregateFilterStatements()) {
				gbArgs.addAggregateFilter(statement);
			}
			rootExecutable.addSubElement(groupByOp);
		}
	
		
		if( ! (orderByClause.getOrderingSequence().isEmpty())) {
			StreamedOrderByOperator orderByOp = new StreamedOrderByOperator(implementationProvider);
			orderByOp.getArgs().setOrderingSequence(orderByClause.getOrderingSequence());
			rootExecutable.addSubElement(orderByOp);
		}
		
		ExecutionPlan execPlan = new ExecutionPlan(rootExecutable);
		return execPlan;
	}
	
}
