package query;

public abstract class AbstractRelationalOperator implements IRelationalOperator{
	
	protected  RelationalOperatorType type;
	
	public RelationalOperatorType getType() {
		return type;
	}
}
