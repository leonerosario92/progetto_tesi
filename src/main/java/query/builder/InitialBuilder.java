package query.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import context.Context;
import dataset.IDataIterator;
import dataset.IEntity;
import model.FieldDescriptor;
import model.IFieldDescriptor;
import model.ITableDescriptor;
import model.TableDescriptor;

public class InitialBuilder {
	
	private Context context;
	private Query query;
	
	private HashSet<TableDescriptor> referencedTables;
	
	
	public InitialBuilder(Context context) {
		
		referencedTables = new HashSet<>();
		
		this.context = context;
		this.query = new Query();
	}

	
	public SelectionBuilder selection(TableDescriptor...args) {
		
		for(TableDescriptor table : args) {
			query.select(new SelectionStatement(table));
		}
		
		return new SelectionBuilder(context,query);
	}
	
	
	public JoinBuilder joinSelection(TableDescriptor factTable) {
		return new JoinBuilder(context,query,factTable);
	}
	
}
