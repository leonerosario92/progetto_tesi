package query.builder.statement;

import java.util.Set;

import model.FieldDescriptor;
import query.builder.LogicalOperand;

public interface CFNode {
	
	boolean hasNextStatement ();

	public void setChainingOperand(LogicalOperand operand);
	
	public LogicalOperand getChainingOperand ();
	
	public String writeSql();
	
}
