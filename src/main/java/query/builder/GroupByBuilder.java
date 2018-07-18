package query.builder;

import java.util.Set;

import com.google.common.collect.Sets;

import context.Context;
import model.FieldDescriptor;
import query.builder.clause.GroupByClause;
import query.builder.clause.ProjectionClause;
import query.builder.predicate.AggregateFunctionType;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.AggregateFilterStatement;
import query.builder.statement.FilterStatement;
import utils.TypeUtils;

public class GroupByBuilder {

	private Context context;
	private Query query;

	
	public GroupByBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	
	public OrderByBuilder orderBy(FieldDescriptor...fields) {
		
		for(FieldDescriptor field : fields) {
			if( ! checkField(field)) {
				//TODO Manage exception properly
				throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
			}
		}
		
		query.orderBy(fields);
		return new OrderByBuilder(context, query);
	}
	
	
	public GroupByBuilder aggregateFilter( AggregateFunctionType aggregateFunction,
									FieldDescriptor field,
									FilterStatementType filterType,
									Object operand ){
		
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		
		AggregateFilterStatement statement =
				new  AggregateFilterStatement(
					aggregateFunction, 
					field, 
					filterType, 
					rightOperand
				);
		
		query.aggregateFilter(statement);
		return this;
	}
	
	
	public Query getQuery() {
		ProjectionClause projectionClause = query.getProjectionClause();
		GroupByClause groupByClause = query.getGroupByClause();
		Set<FieldDescriptor> nonAggregateFields = 
			Sets.difference(
					projectionClause.getReferencedFields(), 
					projectionClause.getAggregateFields()
			);
//		for(FieldDescriptor field : nonAggregateFields) {
//			if(! groupByClause.getReferencedFields().contains(field)) {
//				//TODO : Manage exception properly
//				throw new IllegalArgumentException(
//						"Wrong Syntax : Non-aggregate field " + field.getName() + " cannot be selected for projection.");
//			}
//		}
		
		return query;
	}
	
	
	private boolean checkField(FieldDescriptor field){
		return query.referField(field);
	}
	
	
	private boolean checkAggregateField(FieldDescriptor field) {
		GroupByClause gbClause = query.getGroupByClause();
		if(gbClause.getGroupingSequence().isEmpty()) {
			return false;
		}
		return gbClause.getReferencedFields().contains(field);
	}
	
}
