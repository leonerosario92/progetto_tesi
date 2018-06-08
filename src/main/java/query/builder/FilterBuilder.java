package query.builder;

import context.Context;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import utils.TypeUtils;

public class FilterBuilder {

	private Context context;
	private Query query;
	
	public FilterBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	public FilterBuilder filter (FieldDescriptor field,FilterStatementType type, Object operand) {
		if(! checkField(field)) {
			//TODO manage exception properly
			throw new IllegalArgumentException("Filter statements can only contain fields specified in projection clause");
		}
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		query.filter(statement);
		return this;
	}

	private boolean checkField(FieldDescriptor field){
		return query.referField(field);
	}
	
	public Query getQuery() {
		return query;
	}

}
