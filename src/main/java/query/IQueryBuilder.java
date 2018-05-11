package query;

import dataset.IEntity;
import model.IFieldDescriptor;
import model.ITableDescriptor;
import query.operator.filter.IFilterQueryParams;

public interface IQueryBuilder {
	
	public void filter(IFilterQueryParams params);

	public void project(ITableDescriptor table, IFieldDescriptor field);
	
	public IEntity exec();
}
