package query.execution.operator;

import dataset.IDataSet;

public interface DataSetProcessingFunction <A > {
	
	public IDataSet apply (IDataSet inputSet, A args);
	
}
