package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class TableDescriptor {

	private String name; 
	private ArrayList<FieldDescriptor> fields;
	private int recordCount;
	
	public TableDescriptor(String name) {
		this.name = name;
		this.fields = new ArrayList<FieldDescriptor>();
	}

	public Iterable<FieldDescriptor> getFields() {
		return fields;
	}

	public void addFields(Iterable<FieldDescriptor> newFields) {
		fields.addAll((Collection<FieldDescriptor>) newFields);
	}

	public String getName() {
		return name;
	}

	public FieldDescriptor getField(String name) {
		Iterator<FieldDescriptor> it = fields.iterator();
		FieldDescriptor currentFIeld;
		while(it.hasNext()) {
			currentFIeld = it.next();
			if(currentFIeld.getName().equals(name)) {
				return currentFIeld;
			}
		}
		return null;
	}
	
	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

}
