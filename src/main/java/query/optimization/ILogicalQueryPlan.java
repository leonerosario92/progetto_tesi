package query.optimization;

import java.util.List;

import query.builder.Query;

public interface ILogicalQueryPlan {
	
	public void initializeFromQuery(Query query);
	
	public boolean testRuleApplicability(ITransformationRule rule);
	
	public ILogicalQueryPlan applyTransformationRule(ITransformationRule rule);
	
	public List<ILogicalOperator<?>> getLogicalOperatorSequence();

	public ILogicalOperator<?> getFirstOperator();
	
}
