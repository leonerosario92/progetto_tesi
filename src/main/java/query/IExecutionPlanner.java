package query;

import query.operator.IQueryNode;

public interface IExecutionPlanner {
	
	public  Collection<IExecutionBlock> generateExecutionPlan(Iterable<IQueryNode> queryNodes);
	
}