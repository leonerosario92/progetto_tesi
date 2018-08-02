package query.builder;

import context.Context;
import model.FieldDescriptor;
import model.IDescriptor;
import model.TableDescriptor;
import query.builder.statement.ProjectionStatement;

public class SelectionBuilder {

	private Context context;
	private Query query;
	
	
	public SelectionBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	
	public ProjectionBuilder project (IDescriptor...args) {
		for(IDescriptor field : args) {
			if(! checkValidField(field)) {
				//TODO Manage exception properly
				throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
			}
			query.project(new ProjectionStatement(field));
		}
		return new ProjectionBuilder(context, query);
	}

	
	private boolean checkValidField(IDescriptor field) {
		return query.referTable(field.getTable());
	}
	
}
