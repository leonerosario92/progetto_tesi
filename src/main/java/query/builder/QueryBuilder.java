package query.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import context.Context;
import context.IContext;
import dataset.IDataIterator;
import dataset.IEntity;
import model.FieldDescriptor;
import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.operator.IQueryNode;
import query.operator.filter.FilterQueryNode;
import query.operator.filter.IFilterQueryParams;

public class QueryBuilder {
	
	private Context context;
	private Query query;
	private boolean finalized;
	
	
	public QueryBuilder(Context context) {
		//TODO forzare garbage collection
		this.finalized = false;
		this.context = context;
		this.query = new Query();
	}


	public void filter(FieldDescriptor field, IFilterPredicate predicate) {
		
	}


	public void project(ITableDescriptor table, IFieldDescriptor field) {
		// TODO Auto-generated method stub
		
	}


	public Set<IQueryNode> getQueryPlan() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private void checkFinalized() {
		if(finalized) {
			throw new IllegalStateException();
		}
	}


	public IEntity exec() {
		finalized = true;
		//IQueryProvider qp = context.getQueryProvider();
		Query query = buildQuery();
		IEntity result = context.execQuery(query);
		return result;
		//TODO interporre generazione del piano di esecuzione
	}


	private Query buildQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
