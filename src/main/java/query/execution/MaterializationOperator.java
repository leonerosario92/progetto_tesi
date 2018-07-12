package query.execution;

import java.util.ArrayList;
import java.util.List;

import dataset.IDataSet;
import dataset.ILayoutManager;
import query.ImplementationProvider;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.MaterializationFunction;
import query.execution.operator.RelOperatorType;

public abstract class MaterializationOperator <F extends MaterializationFunction, A extends IOperatorArgs> extends Operator<F,A>{

	private List<IDataSet> inputDataSets;
	
	
	public MaterializationOperator(ImplementationProvider provider, RelOperatorType type) {
		super(provider, type);
		inputDataSets = new ArrayList<>();
	}
	
	
	public  void setInputData(IDataSet...inputData) {
		for(int i=0; i<inputData.length; i++) {
			inputDataSets.add(inputData[i]);
		}
	}

	
	public  IDataSet execOperator(IQueryExecutor executor) throws QueryExecutionException {
		
		if(inputDataSets.size() == 0) {
			throw new IllegalStateException("Materialization Operators cannot be executed if no input DataSet has been Set."); 
		}
		ILayoutManager layoutManager = executor.getlayoutManager();
		return function.apply(inputDataSets,layoutManager,args);
	}

}
