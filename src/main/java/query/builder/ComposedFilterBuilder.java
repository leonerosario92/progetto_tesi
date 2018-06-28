package query.builder;

import context.Context;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.CFNode;
import query.builder.statement.CFilterStatement;
import query.builder.statement.FilterStatement;
import utils.TypeUtils;


public class ComposedFilterBuilder {
	
	private Context context;
	private Query query;
	
	private CFilterStatement composedStatement;

	
	public ComposedFilterBuilder(Context context, Query query, FilterStatement initialStatement) {
		this.context = context;
		this.query = query;
		
		composedStatement = new CFilterStatement(initialStatement);
	}
	
	
	public ComposedFilterBuilder or (CFNode statement) {
		composedStatement.chain(LogicalOperand.OR, statement);
		return this;
	}


	public ComposedFilterBuilder or(FieldDescriptor field, FilterStatementType type, Object operand) {
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		return or(statement);
	}
	
	
	public ComposedFilterBuilder and (CFNode statement) {
		composedStatement.chain(LogicalOperand.AND, statement);
		return this;
	}


	public ComposedFilterBuilder and(FieldDescriptor field, FilterStatementType type, Object operand) {
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		return and(statement);
	}


	public Query getQuery() {
		query.filter(composedStatement);
		return query;
	}
	
}
