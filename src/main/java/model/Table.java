package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Table implements ITable {

	private String name; 
	private ArrayList<IField> fields;
	
	public Table(String name) {
		this.name = name;
		this.fields = new ArrayList<IField>();
	}

	public Iterable<IField> getFields() {
		return fields;
	}

	public void addFields(Iterable<Field> newFields) {
		fields.addAll((Collection<? extends IField>) newFields);
	}

	public String getName() {
		return name;
	}

}
