package query.builder;

import context.Context;
import model.FieldDescriptor;

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
		//TODO check if operand has same type of field 
		FilterStatement statement = type.getInstance(field, operand);
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
