package query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import context.IContext;
import dataset.IDataIterator;
import dataset.IEntity;
import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.operator.IQueryNode;
import query.operator.filter.FilterQueryNode;
import query.operator.filter.IFilterQueryParams;

public class QueryBuilder implements IQueryBuilder{
	
	private IContext context;
	private ArrayList<IQueryNode> nodes;
	private boolean finalized;
	
	
	public QueryBuilder(IContext context) {
		//TODO forzare garbage collection
		this.finalized = false;
		this.context = context;
	}


	public void filter(IFilterQueryParams params) {
		checkFinalized();
		FilterQueryNode node = new FilterQueryNode(params);
		nodes.add(node);
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
		IQuery query = buildQuery();
		IEntity result = context.execQuery(query);
		return result;
		//TODO interporre generazione del piano di esecuzione
	}


	private IQuery buildQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
