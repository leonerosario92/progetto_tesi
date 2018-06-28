package query.builder.statement;

import query.builder.LogicalOperand;

public interface CFNode {
	
	boolean hasNextStatement ();

	public void setChainingOperand(LogicalOperand operand);
	
	public LogicalOperand getChainingOperand ();
	
	public String writeSql();
	
}
