package query.builder;

import java.math.BigDecimal;

import context.Context;
import context.DataType;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;

public class ProjectionBuilder {

	private Context context;
	private Query query;
	
	public ProjectionBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	public Query getQuery () {
		return query;
	}
	
	public FilterBuilder filter (FieldDescriptor field,FilterStatementType type, Object operand) {
		if(! checkField(field)) {
			//TODO manage exception properly
			throw new IllegalArgumentException("Filter statements can only contain fields specified in projection clause");
		}
		//TODO check if operand has same type of field 
		Object rightOperand = parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		query.filter(statement);
		return new FilterBuilder(context,query);
	}


	private boolean checkField(FieldDescriptor field){
		return query.referField(field);
	}

	
	public ProjectionBuilder project(FieldDescriptor field) {
		query.project(new ProjectionStatement(field));
		return this;
	}
	
	
	private Object parseOperand(Object operand, DataType type ) {
		if(!((operand instanceof Number) || (operand instanceof String))){
			throw new IllegalArgumentException("Right Operand of filter statement has invalid type.");
		}
		switch (type) {
		case STRING :
			return parseStringOperand (operand);
		case BIG_DECIMAL:
		case DOUBLE:
		case FLOAT:
		case INTEGER:
		case LONG:
			return parseNumericOperand(operand,type);
		default :
			throw new IllegalStateException("Filter Statements cannot be applied to fields that have a non-comparable type");
		}
	}


	private Object parseStringOperand(Object operand) {
		if (!(operand instanceof String)) {
			throw new IllegalArgumentException("Right operand of filter statement does not match the type of the field to which is applied.");
		}
		return operand;
	}

	
	private Object parseNumericOperand(Object operand, DataType type) {
		if(! (operand instanceof Number)) {
			throw new IllegalArgumentException("Right operand of filter statement does not match the type of the field to which is applied.");
		}
		String literalValue = operand.toString();
		switch (type) {
		case BIG_DECIMAL:
			return new BigDecimal(literalValue);
		case DOUBLE:
			return Double.parseDouble(literalValue);
		case FLOAT:
			return Float.parseFloat(literalValue);
		case INTEGER:
			return Integer.parseInt(literalValue);
		case LONG:
			return Long.parseLong(literalValue);
		default:
			return null;
		}
	}
}