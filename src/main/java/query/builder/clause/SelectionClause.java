package query.builder.clause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.SelectionStatement;

public class SelectionClause {
	
	private ArrayList<SelectionStatement> selectionStatements;
	private HashSet<String> referencedTables;
	
	public SelectionClause () {
		selectionStatements = new ArrayList<>();
		referencedTables = new HashSet<>();
	}
	
	public void addStatement(SelectionStatement statement) {
		selectionStatements.add(statement);
		referencedTables.add(statement.getTable().getName());
	}
	
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.SELECTION_CLAUSE);
		sb = writeSelectionStatements(sb);
		sb.append(QueryConstants.NEWLINE);
		
		return sb.toString();
	}
	

	private StringBuilder writeSelectionStatements(StringBuilder sb) {
		
		Iterator<SelectionStatement> it = selectionStatements.iterator();
		TableDescriptor currentTable;	
		
		while(it.hasNext()) {
			currentTable = it.next().getTable();
			sb.append(QueryConstants.WHITESPACE_CHAR);
			sb.append(currentTable.getName());
			if(it.hasNext()) {
				sb.append(QueryConstants.COMMA_CHAR);	
			}
		}
		return sb;
	}
	
	
	public boolean referTable (TableDescriptor table) {
		return referencedTables.contains(table.getName());
	}
	
	
	public List<TableDescriptor> getReferencedtables (){
		List<TableDescriptor> referencedTables 	= new ArrayList<>();	
		for(SelectionStatement statement : selectionStatements) {
			referencedTables.add(statement.getTable());
		}
		return referencedTables;
	}
	
	
	public List<SelectionStatement> getStatements(){
		return selectionStatements;
	}
}
