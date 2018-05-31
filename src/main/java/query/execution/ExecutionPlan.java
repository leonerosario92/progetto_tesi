package query.execution;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import model.TableDescriptor;


public class ExecutionPlan {
	
	private List<ExecutionPlanItem> itemList;
	private HashSet<FieldDescriptor> referencedFields;
	private HashSet<TableDescriptor> referencedTables;
	
	public ExecutionPlan() {
		itemList = new ArrayList<>();
		this.referencedFields = new HashSet<>();
		this.referencedTables = new HashSet<>();
	}
	
	public void addItem(ExecutionPlanItem item) {
		FieldDescriptor referencedField = item.getReferencedField();
		itemList.add(item);
	}
	
	public List<ExecutionPlanItem> getItemList(){
		return itemList;
	}
	
	public void setReferencedField(FieldDescriptor field) {
		referencedFields.add(field);
	}
	
	public Set<FieldDescriptor> getReferencedFields(){
		return referencedFields;
	}
	
	public void setReferencedTable(TableDescriptor table) {
		referencedTables.add(table);
	}
	
	public Set<TableDescriptor> getReferencedTables(){
		return referencedTables;
	}
}
