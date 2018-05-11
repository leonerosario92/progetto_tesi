package impl.jdbc.mysql.nativeimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import query.IQueryPlan;
import query.ExecutionBlock;
import query.IExecutionBlock;
import query.IExecutionPlanner;
import query.operator.IQueryNode;
import query.operator.RelOperatorType;
import query.operator.filter.FilterQueryNode;
import query.operator.projection.IProjectionQueryParams;
import query.operator.projection.ProjectionQueryNode;

public class JDBCNativeExecutionPlanner implements IExecutionPlanner{
	
	ArrayList<IExecutionBlock> executionPlan;

	public JDBCNativeExecutionPlanner() {
		executionPlan = new ArrayList<>();
	}
	
	@Override
	public Iterable<IExecutionBlock> generateExecutionPlan(Iterable<IQueryNode> queryNodes) {
		ExecutionBlock  block = new ExecutionBlock(
				new ProjectionQueryNode(
						new IProjectionQueryParams()
						)
				);
	}
}
