package query.builder.statement;

import java.util.HashSet;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.QueryConstants;
import query.builder.predicate.FilterStatementType;

public abstract class FilterStatement implements CFNode {
	
	private final FilterStatementType TYPE;
	private final String SQL_REPRESENTATION;
	private FieldDescriptor field; 
	private Object rightOperand;
	private LogicalOperand chainingOperand;
	
	private boolean hasNextStatement;
	
	
	public FilterStatement(FilterStatementType type) {
		this.TYPE = type;
		this.SQL_REPRESENTATION = type.representation;
		this.hasNextStatement = false;
	}

	
	public FilterStatementType getFilterType() {
		return TYPE;
	}

	public void setField(FieldDescriptor field) {
		this.field = field;
	}
	
	public FieldDescriptor getField() {
		return field;
	}

	public void setOperand(Object operand) {
		this.rightOperand = operand;
		if(operand instanceof FieldDescriptor) {
			
		}
	}
	
	public Object getRightOperand() {
		return rightOperand;
	}


	public String writeSql() {
		String tableName = field.getTable().getName();
		String fieldName = field.getName();
		StringBuilder sb = new StringBuilder();
		
		sb.append(tableName).append(QueryConstants.DOT_CHAR).append(fieldName)
			.append(QueryConstants.WHITESPACE_CHAR)
			.append(SQL_REPRESENTATION)
			.append(QueryConstants.WHITESPACE_CHAR)
			.append(rightOperand);
			
		return sb.toString();
	}


	@Override
	public void setChainingOperand(LogicalOperand operand) {
		this.chainingOperand = operand;
		this.hasNextStatement = true;
	}
	
	
	@Override
	public LogicalOperand getChainingOperand() {
		return chainingOperand;
	}


	@Override
	public boolean hasNextStatement() {
		return hasNextStatement;
	}


	@Override
	public Set<FieldDescriptor> getReferencedFields() {
		Set<FieldDescriptor> refFields = new HashSet<>();
		refFields.add(field);
		if(rightOperand!= null && rightOperand instanceof FieldDescriptor) {
			refFields.add((FieldDescriptor) rightOperand);
		}
		return refFields;
	}
	
}
