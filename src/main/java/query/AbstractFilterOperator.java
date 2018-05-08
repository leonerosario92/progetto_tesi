package query;

import java.util.function.Function;

public abstract class AbstractFilterOperator extends AbstractRelationalOperator implements  Function<IQueryResult,String> {
	
	public AbstractFilterOperator() {
		this.type = RelationalOperatorType.FILTER;
	}
	
	
}