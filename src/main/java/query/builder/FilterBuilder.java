package query.builder;

import context.Context;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.builder.statement.ProjectionStatement;
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
			query.project(new ProjectionStatement(field,false));
		}
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		query.filter(statement);
		return this;
	}
	
	
	public Query getQuery() {
		return query;
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
	
	
	public GroupByBuilder groupBy(FieldDescriptor...fields) {
		for(int i=0; i<fields.length; i++) {
			if(! checkField(fields[i])) {
				throw new IllegalArgumentException("Group By statements can only contain fields specified in projection clause");
			}
		}
		query.groupBy(fields);
		return new GroupByBuilder(context,query);
	}
	
	
	public OrderByBuilder orderBy(FieldDescriptor...fields) {
		
		for(FieldDescriptor field : fields) {
			if( ! checkField(field)) {
				//TODO Manage exception properly
				throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
			}
		}
		
		query.orderBy(fields);
		return new OrderByBuilder(context, query);
	}

	
	private boolean checkField(FieldDescriptor field){
		return query.referField(field);
	}

}
