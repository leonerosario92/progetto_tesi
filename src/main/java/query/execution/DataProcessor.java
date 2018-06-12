package query.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.execution.operator.DataSetProcessingFunction;
import query.execution.operator.IOperatorArgs;

public class DataProcessor {
	
	private DataSetProcessingFunction function;
	private List<FieldDescriptor> referencedFields;
	private IOperatorArgs args;
	
	public DataProcessor() {
		referencedFields = new ArrayList<>();
	}
		
	public void setArgs (IOperatorArgs args) {
		this.args = args;
	}
	
	public void setFunction(DataSetProcessingFunction function) {
		this.function = function;
	}

	
	public IDataSet processDataSet(IDataSet inputSet) throws Exception {
		IDataSet result =  (IDataSet) function.apply(inputSet,args);
		return result;
	}
	
}
