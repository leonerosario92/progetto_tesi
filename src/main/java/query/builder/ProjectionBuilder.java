package query.builder;

import java.math.BigDecimal;

import context.Context;
import context.DataType;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
import utils.TypeUtils;

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
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
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
	

}