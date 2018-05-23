package query.builder.clause;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.JoinDescriptor;
import query.builder.JoinStatement;
import query.builder.QueryConstants;
import query.builder.statement.SelectionStatement;

public class SelectionClause {
	
	private ArrayList<SelectionStatement> selectionStatements;
	private ArrayList <JoinStatement> joinStatements;
	private HashSet<String> referencedTables;
	
	public SelectionClause () {
		selectionStatements = new ArrayList<>();
		joinStatements = new ArrayList<>();
		referencedTables = new HashSet<>();
	}
	
	public void addSelectStatement(SelectionStatement statement) {
		selectionStatements.add(statement);
		referencedTables.add(statement.getTable().getName());
	}
	
	public void addJoinStatement(JoinStatement statement) {
		joinStatements.add(statement);
		referencedTables.add(statement.getFactTable().getName());
		for(JoinDescriptor jd : statement.getJoinedTables()) {
			referencedTables.add(jd.getDimension().getName());
		}
	}
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.SELECTION_CLAUSE);
		sb = writeSelectionStatements(sb);
		sb.append(QueryConstants.NEWLINE);
		sb = writeJoinStatements(sb);
		
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
	
	
	/*
	Print a join statement in the following format : 
	[factTable] 
	JOIN[dimensionTable] ON [factKey] = [fieldKey]
	...
	JOIN[dimensionTable] ON [factKey] = [fieldKey]
	*/
	private StringBuilder writeJoinStatements(StringBuilder sb) {
		
		Iterator<JoinStatement> statementIt = joinStatements.iterator();
		JoinStatement currentStatement;
		while (statementIt.hasNext()) {
			currentStatement = statementIt.next();
			
			sb.append(currentStatement.getFactTable().getName());
			sb.append(QueryConstants.WHITESPACE_CHAR);
			
			Iterator<JoinDescriptor> joinIt = currentStatement.getJoinedTables().iterator();
			while(joinIt.hasNext()) {
				JoinDescriptor currentJoin = joinIt.next();
				sb.append(QueryConstants.NEWLINE);
				
				sb.append(QueryConstants.JOIN);
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(currentJoin.getDimension().getName());
				sb.append(QueryConstants.WHITESPACE_CHAR);
				
				sb.append(QueryConstants.ON);
				sb.append(QueryConstants.WHITESPACE_CHAR);
				
				sb.append(currentStatement.getFactTable().getName());
				sb.append(QueryConstants.DOT_CHAR);
				sb.append(currentJoin.getFactKey().getName());
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(QueryConstants.EQUALS_TO);
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(currentJoin.getDimension().getName());
				sb.append(QueryConstants.DOT_CHAR);
				sb.append(currentJoin.getDimensionKey().getName());
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
