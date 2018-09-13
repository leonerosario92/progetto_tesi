package query.optimization;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Sets;

import query.builder.Query;
import query.builder.clause.FilterClause;
import query.builder.clause.ProjectionClause;
import query.builder.clause.SelectionClause;
import query.builder.statement.FilterStatement;

public class LogicalQueryPlan implements ILogicalQueryPlan {
	
	private LinkedList<ILogicalOperator<?>> operatorSequence;
	
	public LogicalQueryPlan() {
		this.operatorSequence = new LinkedList<>();
	}

	@Override
	public void initializeFromQuery(Query query) {
		
		SelectionClause selectionClause = query.getSelectionClause();
		LogicalLoadDataSetOperator logicalSelectionOp = new LogicalLoadDataSetOperator();
		LoadDataSetOperatorArgs selectionArgs = logicalSelectionOp.getArgs();
//		InitializeArgs...
		operatorSequence.addLast(logicalSelectionOp);
		
		
		ProjectionClause projectionClause = query.getProjectionClause();
		LogicalProjectionOperator logicalProjectionOp = new LogicalProjectionOperator();
		ProjectionOperatorArgs projectionArgs = logicalProjectionOp.getArgs();
		
		projectionArgs.setColumns(projectionClause.getReferencedFields());
		
		operatorSequence.addLast(logicalProjectionOp);
		
		
		FilterClause filterClause = query.getFilterClause();
		List<FilterStatement> filterStatements = filterClause.getStatements();
		if( ! (filterStatements.isEmpty())) {
			LogicalFilterOperator logicalFilterOp = new LogicalFilterOperator();
			FilterOperatorArgs filterArgs = logicalFilterOp.getArgs();
			
			filterArgs.setFields(filterClause.getReferencedFields());
			filterArgs.setStatements(Sets.newHashSet(filterStatements));
			
			operatorSequence.addLast(logicalFilterOp);
		}
		
		
//		GroupByClause groupByClause = query.getGroupByClause();
//		if( ! (groupByClause.getGroupingSequence().isEmpty())) {
//			ILogicalOperator logicalGroupByOp;
////			logicalGroupByOp = new ...
//			operatorSequence.addLast(logicalGroupByOp);
//		}
//		
//		OrderByClause orderByClause = query.getOrderByClause();
//		if( ! (orderByClause.getOrderingSequence().isEmpty())) {
//			ILogicalOperator logicalOrderByOp;
////			logicalOrderByOp = new...
//			operatorSequence.addLast(logicalOrderByOp);
//		}
		
//		Projection va applicato sia all'inizio che alla fine
		
	}

	
	@Override
	public List<ILogicalOperator<?>> getLogicalOperatorSequence() {
		return operatorSequence;
	}

	
	@Override
	public boolean testRuleApplicability(ITransformationRule rule) {
		return false;
	}

	
	@Override
	public ILogicalQueryPlan applyTransformationRule(ITransformationRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalOperator<?> getFirstOperator() {
		return operatorSequence.getFirst();
	}

}
