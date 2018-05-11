package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TableDescriptor implements ITableDescriptor {

	private String name; 
	private ArrayList<IFieldDescriptor> fields;
	
	public TableDescriptor(String name) {
		this.name = name;
		this.fields = new ArrayList<IFieldDescriptor>();
	}

	public Iterable<IFieldDescriptor> getFields() {
		return fields;
	}

	public void addFields(Iterable<FieldDescriptor> newFields) {
		fields.addAll((Collection<? extends IFieldDescriptor>) newFields);
	}

	public String getName() {
		return name;
	}

}
