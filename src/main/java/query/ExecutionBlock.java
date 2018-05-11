package query;

import java.util.ArrayList;
import java.util.Collection;

import query.operator.IQueryNode;

public class ExecutionBlock implements IExecutionBlock{

	ArrayList<IQueryNode> nodes;
	
	public ExecutionBlock() {
		this.nodes = new ArrayList<>();
	}
	
	public ExecutionBlock(IQueryNode node) {
		this ();
		nodes.add(node);
	}
	
	public ExecutionBlock(Collection<IQueryNode> nodes) {
		this.nodes.addAll(nodes);
	}
	
	@Override
	public int getOperationsNumber() {
		return nodes.size();
	}

	@Override
	public Collection<IQueryNode> getOperations() {
		return nodes;
	}
	
}
