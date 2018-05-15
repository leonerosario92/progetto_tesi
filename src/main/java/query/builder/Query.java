package query.builder;

import java.util.ArrayList;

import model.FieldDescriptor;
import model.TableDescriptor;

public class Query {

	private SelectClause selectClause;
	private FromClause fromClause;
	private FilterClause whereClause;
	
	
	public Query() {
		this.selectClause = new SelectClause();
		this.fromClause = new FromClause();
		this.whereClause = new FilterClause();
	}
	
	
	public void select (SelectStatement statement) {
		
	}
	
	
	public void project (ProjectionStatement statement) {
		
	}
	
	
	public void filter (IFilterStatement statement) {
		
	}
	
	
	public String writeSql() {
		
	}
}
