package query.builder.statement;

import java.util.HashSet;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.QueryConstants;
import query.builder.predicate.AggregateFunction;
import query.builder.predicate.FilterStatementType;

public class AggregateFilterStatement /*implements CFNode*/ {
	
	private final String FILTER_OPERAND_REPRESENTATION ;

	private AggregationDescriptor descriptor;
	private FilterStatement filterStatement;
	
//	private LogicalOperand chainingOperand; 
//	private boolean hasNextStatement;
	
	public AggregateFilterStatement(
			AggregateFunction aggregateFunction,
			FieldDescriptor field,
			FilterStatementType filterType,
			Object rightOperand
	){
		this.FILTER_OPERAND_REPRESENTATION = filterType.representation;
		this.filterStatement = filterType.getInstance(field, rightOperand);
		this.descriptor = new AggregationDescriptor(field, aggregateFunction);
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
	

	//Override
	public Set<FieldDescriptor> getReferencedFields() {
		return filterStatement.getReferencedFields();
	}
	
	
	public String writeSql() {
		String tableName = filterStatement.getField().getTable().getName();
		String fieldName = filterStatement.getField().getName();
		StringBuilder sb = new StringBuilder();
			
		sb.append(this.descriptor.toString())
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(FILTER_OPERAND_REPRESENTATION)
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(filterStatement.getRightOperand());
		
		return sb.toString(); 
	}


	public AggregationDescriptor getAggregationDescriptor() {
		return descriptor;
	}
	
}
