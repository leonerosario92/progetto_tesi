package query.execution;

import dataset.IDataSet;
import dataset.ILayoutManager;
import query.ImplementationProvider;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.MaterializationFunction;
import query.execution.operator.RelOperatorType;

public abstract class MaterializationOperator <F extends MaterializationFunction, A extends IOperatorArgs> extends Operator<F,A>{

	public MaterializationOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type);
	}
	
	public IDataSet buildDataSet(Iterable<IDataSet> inputDataSets,ILayoutManager layoutmanager) throws Exception {
		IDataSet result =  (IDataSet) function.apply(inputDataSets,layoutmanager,args);
		return result;
	}
}
