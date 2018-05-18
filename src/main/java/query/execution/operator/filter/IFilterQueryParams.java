package query.execution.operator.filter;

import java.util.function.Predicate;

import model.FieldDescriptor;
import query.execution.operator.IQueryParams;

public interface IFilterQueryParams extends IQueryParams {
	
	public FieldDescriptor getField();
	
	public Predicate<?>[] getPredicates();
	
}