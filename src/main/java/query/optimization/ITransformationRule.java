package query.optimization;

import java.util.List;
import java.util.Set;

import query.execution.operator.LogicalOperatorType;

public interface ITransformationRule {
	
	List<LogicalOperatorType> getApplicabilityPattern();
	
	Set<ILogicalQueryPlan> applyRule(ILogicalQueryPlan inputPlan);

}
