package query;

public abstract class AbstractQueryNode implements IQueryNode {
	
private RelOperatorType type;
	
	public AbstractQueryNode(RelOperatorType type) {
		this.type = type;
	}
	
	public RelOperatorType getOperatorType() {
		return type;
	}
}