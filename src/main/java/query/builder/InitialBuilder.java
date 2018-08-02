package query.builder;


import context.Context;
import model.TableDescriptor;
import query.builder.statement.SelectionStatement;

public class InitialBuilder {
	
	private Context context;
	private Query query;
	
	public InitialBuilder(Context context) {
		
		this.context = context;
		this.query = new Query();
	}

	
	public SelectionBuilder select(TableDescriptor...args) {
		
		for(TableDescriptor table : args) {
			query.select(new SelectionStatement(table));
		}
		
		return new SelectionBuilder(context,query);
	}
	
	
}
