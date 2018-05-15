package query.builder;

import model.FieldDescriptor;

public abstract class ComparisonFilterStatement implements IFilterStatement /*<T>*/{
	
	
	private final PredicateType TYPE;
	private final String SQL_REPRESENTATION;
	private FieldDescriptor field; 
	private Object /*T*/ rightOperand;
	
	
	public ComparisonFilterStatement(PredicateType type, FieldDescriptor field, Object operand) {
		this.TYPE = type;
		this.SQL_REPRESENTATION = type.representation;
		this.field = field;
		this.rightOperand =  operand;
	}
	
	
	@Override
	public PredicateType getPredicateType() {
		return TYPE;
	}


	@Override
	public FieldDescriptor getField() {
		return field;
	}


	@Override
	public Object /*T*/ getRightOperand() {
		return rightOperand;
	}


	@Override
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
