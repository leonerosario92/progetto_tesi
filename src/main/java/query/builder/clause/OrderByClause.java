package query.builder.clause;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.FieldDescriptor;
import query.builder.QueryConstants;
import query.builder.statement.CFilterStatement;

public class OrderByClause {
	
	private List<FieldDescriptor> orderingSequence;
	
	public OrderByClause() {
		orderingSequence = new LinkedList<>();
	}
	
	
	public void addStatement(FieldDescriptor...fields) {
		for(int i=0; i<fields.length; i++) {
			orderingSequence.add(i, fields[i]);
		}
	}
	
	
	public List<FieldDescriptor> getOrderingSequence (){
		return orderingSequence;
	}
	
	
	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(QueryConstants.ORDER_BY_CLAUSE);
		sb.append(QueryConstants.WHITESPACE_CHAR);
		
		Iterator<FieldDescriptor> it = orderingSequence.iterator();
		FieldDescriptor field;
		if(it.hasNext()) {
			field = it.next();
			appendField(sb, field);
		}
		while (it.hasNext()) {
			field = it.next();
			sb.append(QueryConstants.WHITESPACE_CHAR)
			.append(QueryConstants.COMMA_CHAR)
			.append(QueryConstants.WHITESPACE_CHAR);
			appendField(sb, field);
		}
		
		return sb.toString();
	}
	
	
	public StringBuilder appendField (StringBuilder sb,FieldDescriptor field) {
		return 	sb.append(field.getTable().getName() +"."+ field.getName());
	}

}
