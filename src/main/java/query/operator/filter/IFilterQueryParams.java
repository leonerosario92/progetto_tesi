package query.operator.filter;

import java.util.function.Predicate;

import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.operator.IQueryParams;

public interface IFilterQueryParams extends IQueryParams {
	
	public ITableDescriptor getTable();
	public IFieldDescriptor getField();
	public Predicate<?> getPredicate();
	
}