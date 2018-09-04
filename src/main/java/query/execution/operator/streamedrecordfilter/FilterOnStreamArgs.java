package query.execution.operator.streamedrecordfilter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.statement.CFNode;
import query.execution.operator.IOperatorArgs;

public class FilterOnStreamArgs implements IOperatorArgs {
	
	private Set<CFNode> statements;
	private Set<FieldDescriptor> fields;
	
	
	public FilterOnStreamArgs() {
		statements = new HashSet<>();
		fields = new HashSet<>();
	}


	public Set<CFNode> getStatements() {
		return statements;
	}


	public void setStatements(Set<CFNode> statements) {
		this.statements = statements;
	}


	public Set<FieldDescriptor> getFields() {
		return fields;
	}


	public void setFields(Set<FieldDescriptor> fields) {
		this.fields = fields;
	}
	
	@Override
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("Statements = { ");
		Iterator<CFNode> it = statements.iterator();
		while (it.hasNext()) {
			CFNode statement = it.next();
			sb.append(statement.writeSql());
			if(it.hasNext()) {
				sb.append(" , ");
			}
			sb.append(" }");
		}
		return sb.toString();			
	}

}
