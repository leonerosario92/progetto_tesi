package query.operator;

public abstract class AbstractRelOperator implements IRelOperator{
	
	protected  RelOperatorType type;
	
	protected AbstractRelOperator(RelOperatorType type) {
		this.type = type;
	}
	
	public RelOperatorType getType() {
		return type;
	}
}
