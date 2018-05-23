package query.execution;

import java.util.concurrent.Callable;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.IOperatorFunction;

public class ExecutionPlanItem implements Callable<IDataSet> {
	
	private IOperatorFunction function;
	private IDataSet inputDataSet;
	private FieldDescriptor referencedField;
	private IOperatorArgs args;
	
	public void setInputDataSet(IDataSet inputDataSet) {
		this.inputDataSet = inputDataSet;
	}
	
	public void setArgs (IOperatorArgs args) {
		this.args = args;
	}
	
	public void setFunction(IOperatorFunction function) {
		this.function = function;
	}
	
	public FieldDescriptor getReferencedField() {
		return referencedField;
	}

	public void setReferencedField(FieldDescriptor referencedField) {
		this.referencedField = referencedField;
	}

	@Override
	public IDataSet call() throws Exception {
		if((function == null) || (inputDataSet == null) || (args == null)) {
			//TODO Manage exception properly
			throw new IllegalStateException();
		}
		return (IDataSet) function.apply(args);
	}
	
}
