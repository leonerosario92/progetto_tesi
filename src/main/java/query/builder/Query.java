package query.builder;

import java.util.ArrayList;

import model.FieldDescriptor;
import model.TableDescriptor;

public class Query {

	private ArrayList<SelectStatement> selectStatements;
	private ArrayList<ProjectStatement> projectStatements;
	private ArrayList <FilterStatement> filterStatements;
	
	
	private Query() {
		selectStatements = new ArrayList<>();
		projectStatements = new ArrayList<>();
		filterStatements = new ArrayList<>();
	}
	
	
	public void select (TableDescriptor table ) {
		
	}
	
	
	public void project (FieldDescriptor field) {
		
	}
	
	
	public void filter (FieldDescriptor field, IFilterPredicate<?> predicate) {
		
	}
	
	
	public ArrayList<SelectStatement> getSelectStatements(){
		return selectStatements;
		
	}
	
	
	public ArrayList<ProjectStatement> getProjectStatements(){
		return projectStatements;
	}
	
	
	public ArrayList<FilterStatement> getFilterStatements(){
		return filterStatements;
	}
}
