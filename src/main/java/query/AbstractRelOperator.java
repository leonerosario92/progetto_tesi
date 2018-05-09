package query;

public abstract class AbstractRelOperator implements IRelOperator{
	
	protected  RelOperatorType type;
	
	public AbstractRelOperator(RelOperatorType type) {
		this.type = type;
	}
	
	public RelOperatorType getType() {
		return type;
	}
}
