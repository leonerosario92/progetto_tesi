package impl.jdbc.mysql.nativeimpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import query.AbstractQueryProvider;
import query.IExecutionBlock;
import query.IQueryProvider;
import query.operator.IQueryNode;
import query.operator.IRelOperator;
import query.operator.RelOperatorType;
import query.operator.filter.IFilterQueryParams;
import query.operator.projection.IProjectionQueryParams;
import query.operator.projection.ProjectionQueryNode;

public  class JDBCNativeQueryProvider extends AbstractQueryProvider{
	
	public JDBCNativeQueryProvider(IDataContext context) {
		super(context);
	}


	@Override
	public IDataIterator execBlock(IDataContext context, IExecutionBlock block) {
		
		List<IQueryNode> projectionOperations;
		List<IQueryNode> filterOperations;
		
		Collection<IQueryNode> queryNodes = block.getOperations();
		
		//Group queries by implemented function
		ListMultimap<RelOperatorType, IQueryNode> nodesByOperator = Multimaps.index(queryNodes,
			    new Function<IQueryNode, RelOperatorType>() {
			      public RelOperatorType apply(IQueryNode node) {
			        return node.getOperatorType();
			      }
			    });
		
		if(nodesByOperator.containsKey(RelOperatorType.PROJECTION)) {
			projectionOperations = nodesByOperator.get(RelOperatorType.PROJECTION);
		}
		
		if(nodesByOperator.containsKey(RelOperatorType.FILTER)) {
			filterOperations = nodesByOperator.get(RelOperatorType.FILTER);
		}
		
		String statement = generateSqlStatement(projectionOperations, filterOperations);
}


	private String generateSqlStatement(List<IQueryNode> projectionOperations, List<IQueryNode> filterOperations) {
		// TODO Auto-generated method stub
		return null;
	}
