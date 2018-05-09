package query;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;

import datacontext.DataContext;
import model.IField;
import model.ITable;

public class QueryBuilder implements IQueryBuilder{
	
	private DataContext context;
	private ArrayList<IQueryNode> nodes;
	private boolean finalized;
	
	
	public QueryBuilder(DataContext context) {
		//TODO forzare garbage collection
		this.finalized = false;
		this.context = context;
	}


	public void filter(ITable table, IField field, Predicate<?> condition) {
		checkFinalized();
		FilterQueryNode node = new FilterQueryNode();
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

	
}
