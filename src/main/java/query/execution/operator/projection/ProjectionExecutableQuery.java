package query.execution.operator.projection;

import dataset.IDataSet;
import query.execution.AbstractExecutableQuery;
import query.execution.operator.IQueryParams;

public class ProjectionExecutableQuery extends AbstractExecutableQuery{
	
	ProjectionFunction function;
	IDataSet inputSet;
	IProjectionQueryParams params;
	
	public ProjectionExecutableQuery(ProjectionFunction function) {
		this.function = function;
	}
	
	@Override
	public void setInputDataSet(IDataSet inputSet) {
		this.inputSet = inputSet;
	}

	@Override
	public void setParams(IQueryParams params) {
		if(!(params instanceof IProjectionQueryParams)) {
			//TODO manage exception properly
			throw new IllegalArgumentException();
		}
		this.params = (IProjectionQueryParams) params;
	}

	@Override
	public IDataSet execQuery() {
		return function.applyFunction(params, inputSet);
	}
	
}
