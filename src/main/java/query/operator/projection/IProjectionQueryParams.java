package query.operator.projection;

import java.util.function.Predicate;

import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.operator.IQueryParams;

public class IProjectionQueryParams {
	public interface IFilterQueryParams extends IQueryParams {
		
		public ITableDescriptor getTable();
		public IFieldDescriptor getField();
		
	}
}
