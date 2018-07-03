package query.builder.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.QueryConstants;
import query.builder.predicate.FilterStatementType;
import utils.TypeUtils;

public class CFilterStatement implements CFNode {
	
	private List<CFNode> statementSequence;
	private LogicalOperand chainingOperand;
	private boolean hasNextStatement;
	
	public CFilterStatement(FilterStatement initialStatement) {
		statementSequence = new ArrayList<CFNode>();
		statementSequence.add(initialStatement);
		hasNextStatement = false;
	}
	

	public CFilterStatement(FieldDescriptor field, FilterStatementType type, Object operand) {
		statementSequence = new ArrayList<CFNode>();
		
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		statementSequence.add(statement);
	}


	public String writeSql() {
		StringBuilder sb = new StringBuilder();
		
		for (CFNode node : statementSequence) {
			
			sb.append(QueryConstants.OPEN_BRACKET);
			sb.append(node.writeSql());
			sb.append(QueryConstants.CLOSE_BRACKET);
			
			if(node.hasNextStatement()) {
				sb.append(QueryConstants.WHITESPACE_CHAR);
				sb.append(node.getChainingOperand().getSqlRepresentation());
				sb.append(QueryConstants.WHITESPACE_CHAR);
			}
		}
		return sb.toString();
	}
	
	
	public Set<FieldDescriptor> getReferencedFields() {
		HashSet<FieldDescriptor> result = new HashSet<>();
		for(CFNode node : statementSequence) {
			if(node instanceof CFilterStatement) {
				Set<FieldDescriptor> subStatementRefFields = ((CFilterStatement)node).getReferencedFields();
				for(FieldDescriptor fd : subStatementRefFields) {
					result.add(fd);
				}
			}else if(node instanceof FilterStatement) {
				result.add( ((FilterStatement)node).getField());
			}
		}
		return result;
	}
	
	
	public List<CFNode> getstatementSequence(){
		return statementSequence;
	}

	
	public CFilterStatement and(FieldDescriptor field, FilterStatementType type, Object operand) {
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		this.chain (LogicalOperand.AND, statement);
		return this;
	}
	
	
	public CFilterStatement or(FieldDescriptor field, FilterStatementType type, Object operand) {
		Object rightOperand = TypeUtils.parseOperand(operand,field.getType());
		FilterStatement statement = type.getInstance(field, rightOperand);
		chain (LogicalOperand.OR, statement);
		return this;
	}
	
	
	public void chain(LogicalOperand chainingOperand, CFNode statement) {
		CFNode lastElement = statementSequence.get(statementSequence.size() -1);
		lastElement.setChainingOperand(chainingOperand);
		statementSequence.add(statement);
	}


	@Override
	public void setChainingOperand(LogicalOperand operand) {
		this.hasNextStatement = true;
		this.chainingOperand = operand;
	}
	
	
	@Override
	public LogicalOperand getChainingOperand() {
		return chainingOperand;
	}


	@Override
	public boolean hasNextStatement() {
		return hasNextStatement;
	}

}
