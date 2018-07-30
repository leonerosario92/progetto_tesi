package query.execution.operator.loadmaterialized;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.statement.CFNode;
import query.execution.operator.IOperatorArgs;

public class LoadMaterializedArgs implements IOperatorArgs {

	private Set<FieldDescriptor> columns;
	private Set<CFNode> filterStatements;
	
	public LoadMaterializedArgs() {
		columns= new HashSet<>();
		filterStatements = new HashSet<>();
	}
	
	public Set<FieldDescriptor> getColumns(){
		return columns;
	}
	
	public void setColumns(Set<FieldDescriptor> fields) {
		for(FieldDescriptor column : fields) {
			columns.add(column);
		}
	}
	
	public Set<CFNode> getFilterStatements() {
		return filterStatements;
	}

	public void setFilterStatements(Set<CFNode> statements) {
		this.filterStatements = statements;
	}
	
	@Override
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Columns = [");
		Iterator<FieldDescriptor> fieldIt = columns.iterator();
		FieldDescriptor column;
		while(fieldIt.hasNext()) {
			column = fieldIt.next();
			sb.append(" " + column.getName());
			if(fieldIt.hasNext()){
				sb.append(" , ");
			}
		}
		sb.append(" ] ");
		
		sb.append(" , ");
		
		sb.append(" FilterStatements = { ");
		Iterator<CFNode> statementsIt = filterStatements.iterator();
		while (statementsIt.hasNext()) {
			CFNode statement = statementsIt.next();
			sb.append(statement.writeSql());
			if(statementsIt.hasNext()) {
				sb.append(" , ");
			}
			sb.append(" }");
		}
		return sb.toString();
	}

}
