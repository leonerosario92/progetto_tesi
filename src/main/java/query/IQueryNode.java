package query;

public interface IQueryNode {
	
	public RelOperatorType getOperatorType();
	public IQueryParams getParams();
	
}
