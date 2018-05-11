package query.operator;

public interface IQueryNode {

	public RelOperatorType getOperatorType();
	
	public IQueryParams getParams();
	
}
