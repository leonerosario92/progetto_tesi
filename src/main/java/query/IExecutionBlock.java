package query;

import java.util.Collection;

import query.operator.IQueryNode;

public interface IExecutionBlock {
	public int getOperationsNumber();
	public Collection<IQueryNode> getOperations();
}
