//package query.builder;
//
//import context.Context;
//import model.FieldDescriptor;
//import model.TableDescriptor;
//import query.builder.statement.ProjectionStatement;
//
//public class JoinBuilder {
//
//	private Context context;
//	private Query query;
//	private TableDescriptor factTable;
//	private JoinStatement statement;
//	
//	public JoinBuilder(Context context, Query query, TableDescriptor factTable) {
//		this.context = context;
//		this.query = query;
//		this.statement = new JoinStatement(factTable);
//	}
//	
//	
//	public JoinBuilder joinWith (TableDescriptor dimension, FieldDescriptor factKey, FieldDescriptor dimensionKey) {
//		statement.joinWith(dimension, factKey, dimensionKey);
//		return this;
//	}
//	
//	
//	public ProjectionBuilder project (FieldDescriptor...args) {
//		
//		query.join(statement);
//		
//		for(FieldDescriptor field : args) {
//			if(! checkValidField(field)) {
//				//TODO Manage exception properly
//				throw new IllegalArgumentException("projection arguments can only be fields of selected tables");
//			}
//			query.project(new ProjectionStatement(field));
//		}
//		return new ProjectionBuilder(context, query);
//	}
//	
//	
//	private boolean checkValidField(FieldDescriptor field) {
//		return query.referTable(field.getTable());
//	}
//	
//}
