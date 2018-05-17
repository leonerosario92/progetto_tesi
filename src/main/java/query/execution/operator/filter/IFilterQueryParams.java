package query.execution.operator.filter;

import java.util.function.Predicate;

import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.execution.operator.IQueryParams;

public interface IFilterQueryParams extends IQueryParams {
	
	public IFieldDescriptor getField();
	
	public Predicate<?>[] getPredicates();
	
}