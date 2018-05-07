package model;

import java.util.HashSet;
import java.util.Set;

public class Table implements ITable {

	private String name; 
	private Set<IField> fields;
	
	public Table(String name) {
		this.name = name;
		this.fields = new HashSet<IField>();
	}

	public Set<IField> getFields() {
		return fields;
	}

}
