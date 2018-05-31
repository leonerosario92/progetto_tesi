package impl.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import query.execution.ExecutionPlanItem;
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class BaseQueryPlanner extends QueryPlanner {
	
	public BaseQueryPlanner() {
		super();
	}

	/*Base implementation of a queryPlanner. Generate an execution plan where 
	 * there is an execution item for every specific field that execute all 
	 * filter statements that refer to that field.
	 */
	
	@Override
	public ExecutionPlan getExecutionPlan(Query query) {
		
		SelectionClause selectionClause = query.getSelectionClause();
		ProjectionClause projectionClause = query.getProjectionClause();
		FilterClause filterClause = query.getFilterClause();
		
		ExecutionPlan result = new ExecutionPlan();
		
		setReferencedTables(result,selectionClause);
		setReferencedFields(result,projectionClause);
		setFilterStatements(result,filterClause);
		
		return result;
		
	}
	
	
	private void setReferencedTables(ExecutionPlan result, SelectionClause selectionClause) {
		for(SelectionStatement statement : selectionClause.getStatements()) {
			result.setReferencedTable(statement.getTable());
		}
	}

	
	private void setReferencedFields(ExecutionPlan result, ProjectionClause projectionClause) {
		for(ProjectionStatement statement : projectionClause.getStatements()) {
			result.setReferencedField(statement.getField());
		}
	}

	
	private void setFilterStatements(ExecutionPlan result, FilterClause filterClause) {
		Map<FieldDescriptor, List<FilterStatement>> groupedStatements =
				statementsByField(filterClause.getStatements());
		
		groupedStatements.entrySet().forEach(
				(pair)->{
					ExecutionPlanItem item = new ExecutionPlanItem();
					FilterScanFunction function = queryProvider.getFilterScanImpl();
					item.setFunction(function);
					
					FilterScanArgs args = new FilterScanArgs();
					FieldDescriptor referencedField = pair.getKey();
					args.setField(referencedField);
					item.setReferencedField(referencedField);
					args.setStatements(pair.getValue());
					item.setArgs(args);
					
					result.addItem(item);
				});
	}

	
	private Map<FieldDescriptor,List<FilterStatement>> 
		statementsByField(List<FilterStatement> statements){
		HashMap<FieldDescriptor,List<FilterStatement>> groupedStatements =
				new HashMap<> ();
		for(FilterStatement statement : statements) {
			FieldDescriptor field = statement.getField();
			if(groupedStatements.containsKey(field)) {
				groupedStatements.get(field).add(statement);
			}else {
				ArrayList<FilterStatement> l = new ArrayList<>();
				l.add(statement);
				groupedStatements.put(field, l);
			}
		}
		return groupedStatements;
	}
	
	
	
}
