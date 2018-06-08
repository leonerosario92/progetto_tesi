package utils;

import java.math.BigDecimal;

import context.DataType;

public class TypeUtils {

	public static Object parseOperand(Object operand, DataType type ) {
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


	private static Object parseStringOperand(Object operand) {
		if (!(operand instanceof String)) {
			throw new IllegalArgumentException("Right operand of filter statement does not match the type of the field to which is applied.");
		}
		return operand;
	}

	
	private static Object parseNumericOperand(Object operand, DataType type) {
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
