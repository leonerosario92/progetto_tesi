package query.operator.projection;

import model.IFieldDescriptor;
import query.operator.IQueryParams;

public interface IProjectionQueryParams extends IQueryParams {
	
	public IFieldDescriptor[] getFileds ();
		
}
