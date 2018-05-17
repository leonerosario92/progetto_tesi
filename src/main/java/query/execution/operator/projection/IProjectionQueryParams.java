package query.execution.operator.projection;

import model.IFieldDescriptor;
import query.execution.operator.IQueryParams;

public interface IProjectionQueryParams extends IQueryParams {
	
	public IFieldDescriptor[] getFileds ();
		
}
