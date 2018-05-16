package query.builder;

import model.FieldDescriptor;

public abstract class FilterStatement {
	
	
	private final FilterStatementType TYPE;
	private final String SQL_REPRESENTATION;
	private FieldDescriptor field; 
	private Object rightOperand;
	
	
	public FilterStatement(FilterStatementType type) {
		this.TYPE = type;
		this.SQL_REPRESENTATION = type.representation;
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
	
}
