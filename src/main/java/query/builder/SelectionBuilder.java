package query.builder;

import context.Context;
import model.FieldDescriptor;
import model.TableDescriptor;

public class SelectionBuilder {

	private Context context;
	private Query query;
	
	
	public SelectionBuilder(Context context, Query query) {
		this.context = context;
		this.query = query;
	}
	
	
	public ProjectionBuilder project (FieldDescriptor...args) {
		for(FieldDescriptor field : args) {
			if(! checkValidField(field)) {
				//TODO Manage exception properly
				throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
			}
			query.project(new ProjectionStatement(field));
		}
		return new ProjectionBuilder(context, query);
	}
	
	
	private boolean checkValidTable(TableDescriptor table) {
		return query.referTable(table);
	}
	
	private boolean checkValidField(FieldDescriptor field) {
		return query.referTable(field.getTable());
	}
}
