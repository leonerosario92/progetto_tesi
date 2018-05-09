package query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import model.IField;
import model.ITable;
import query.filter.FilterQueryNode;
import query.filter.IFilterQueryParams;

public class QueryBuilder implements IQueryBuilder{
	
	private IDataContext context;
	private ArrayList<IQueryNode> nodes;
	private boolean finalized;
	
	
	public QueryBuilder(IDataContext context) {
		//TODO forzare garbage collection
		this.finalized = false;
		this.context = context;
	}


	public void filter(IFilterQueryParams params) {
		checkFinalized();
		FilterQueryNode node = new FilterQueryNode(params);
		nodes.add(node);
	}


	public void project(ITable table, IField field) {
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


	public IDataIterator execute() {
		
		finalized = true;
		IQueryProvider qp = context.getQueryProvider();
		
		//TODO interporre generazione del piano di esecuzione
		Iterator<IQueryNode> it = nodes.iterator();
		while(it.hasNext()) {
			IQueryNode node = it.next();
			qp.exec(context, node);
		}
	}

	
}
