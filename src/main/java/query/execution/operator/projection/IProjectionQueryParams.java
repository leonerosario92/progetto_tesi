package query.execution.operator.projection;

import model.FieldDescriptor;
import query.execution.operator.IQueryParams;

public interface IProjectionQueryParams extends IQueryParams {
	
	public FieldDescriptor[] getFileds ();
		
}
