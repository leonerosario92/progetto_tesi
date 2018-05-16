package query.builder;

import java.util.ArrayList;

import model.FieldDescriptor;
import model.TableDescriptor;

public class JoinStatement {
	
	private TableDescriptor factTable;
	private ArrayList<JoinDescriptor> joinedTables;

	public JoinStatement(TableDescriptor table) {
		joinedTables = new ArrayList<>();
		factTable = table;
	}

	public TableDescriptor getFactTable() {
		return factTable;
	}

	public ArrayList<JoinDescriptor> getJoinedTables() {
		return joinedTables;
	}

	public void joinWith (TableDescriptor dimension, FieldDescriptor factKey, FieldDescriptor dimensionKey) {
		joinedTables.add(new JoinDescriptor(dimension, factKey, dimensionKey));
	}

}
