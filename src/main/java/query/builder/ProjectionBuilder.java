package query.builder;

import java.math.BigDecimal;

import context.Context;
import datatype.DataType;
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
	
	
	public ProjectionBuilder project(FieldDescriptor field) {
		if( ! checkTable(field)) {
			//TODO Manage exception properly
			throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
		}
		query.project(new ProjectionStatement(field));
		return this;
	}
	
	
	private boolean checkTable(FieldDescriptor field) {
		return query.referTable(field.getTable());
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

	
	public ComposedFilterBuilder composedfilter(FieldDescriptor field, FilterStatementType type, Object operand) {
		if(! checkField(field)) {
			//TODO manage exception properly
			throw new IllegalArgumentException("Filter statements can only contain fields specified in projection clause");
		}
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		return new ComposedFilterBuilder(context,query, statement);
	}
	
	
	public OrderByBuilder orderBy(FieldDescriptor...fields) {
		query.orderBy(fields);
		return new OrderByBuilder(context, query);
	}
	

	private boolean checkField(FieldDescriptor field){
		return query.referField(field);
	}

}