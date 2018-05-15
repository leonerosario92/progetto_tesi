package query.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import context.Context;
import dataset.IDataIterator;
import dataset.IEntity;
import model.FieldDescriptor;
import model.IFieldDescriptor;
import model.ITableDescriptor;

public class QueryBuilder {
	
	private Context context;
	private Query query;
	
	
	public QueryBuilder(Context context) {
		//TODO forzare garbage collection
		this.context = context;
		this.query = new Query();
	}


	public void filter(FieldDescriptor field, PredicateType type, Object operand) {
		
		IFilterStatement statement;
		
		switch (type) {
		case GREATER_THAN :
			IFilterStatement<?> st = new GreaterThan<>(field,operand);
		}
			
		
		
		
	}


	public void project(ITableDescriptor table, IFieldDescriptor field) {
		// TODO Auto-generated method stub
	}
	
	
	public Query buildQuery() {
		return query;
	}

	
}
