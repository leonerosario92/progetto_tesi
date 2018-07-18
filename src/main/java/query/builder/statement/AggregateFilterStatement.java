package query.builder.statement;

import java.util.HashSet;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.QueryConstants;
import query.builder.predicate.AggregateFunctionType;
import query.builder.predicate.FilterStatementType;

public class AggregateFilterStatement /*implements CFNode*/ {
	
	private final String FILTER_OPERAND_REPRESENTATION ;
	
	private AggregateFunctionType AGGREGATE_FUNCTION;

	private FilterStatement filterStatement;
	
//	private LogicalOperand chainingOperand; 
//	private boolean hasNextStatement;
	
	
	public AggregateFilterStatement(
			AggregateFunctionType aggregateFunction,
			FieldDescriptor field,
			FilterStatementType filterType,
			Object rightOperand
	){
		this.FILTER_OPERAND_REPRESENTATION = filterType.representation;
		this.AGGREGATE_FUNCTION = aggregateFunction;
		this.filterStatement = filterType.getInstance(field, rightOperand);
	}
	
	
	public FilterStatementType getFilterStatementType() {
		return filterStatement.getFilterType();
	}
	
	
	public void setField(FieldDescriptor field) {
		this.filterStatement.setField(field);
	}
	
	
	public FieldDescriptor getField() {
		return filterStatement.getField();
	}
	
	
	public void setRightOperand(Object operand) {
		this.filterStatement.setOperand(operand);
	}
	
	
	public Object getRighOperand() {
		return this.filterStatement.getRightOperand();
	}
	
	
	public AggregateFunctionType getAggregateFunctionType() {
		return AGGREGATE_FUNCTION;
	}
	

	//@Override
	public Set<FieldDescriptor> getReferencedFields() {
		return filterStatement.getReferencedFields();
	}
	
	
	public String writeSql() {
		String tableName = filterStatement.getField().getTable().getName();
		String fieldName = filterStatement.getField().getName();
		StringBuilder sb = new StringBuilder();
			
		sb.append(this.AGGREGATE_FUNCTION.representation)
		.append(QueryConstants.OPEN_BRACKET)
		.append(tableName).append(QueryConstants.DOT_CHAR).append(fieldName)	
		.append(QueryConstants.CLOSE_BRACKET)
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(FILTER_OPERAND_REPRESENTATION)
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(filterStatement.getRightOperand());
		
		return sb.toString(); 
	}
	
	

}
