package query.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;
import query.execution.operator.IOperatorFunction;

public class ExecutionPlanItem implements Callable<IDataSet> {
	
	private IOperatorFunction function;
	private IDataSet inputDataSet;
	private List<FieldDescriptor> referencedFields;
	private IOperatorArgs args;
	
	public ExecutionPlanItem() {
		referencedFields = new ArrayList<>();
	}
	
	public void setInputDataSet(IDataSet inputDataSet) {
		this.inputDataSet = inputDataSet;
	}
	
	public void setArgs (IOperatorArgs args) {
		this.args = args;
	}
	
	public void setFunction(IOperatorFunction function) {
		this.function = function;
	}
	
	public List<FieldDescriptor> getReferencedFields() {
		return referencedFields;
	}

	public void setReferencedField(FieldDescriptor field) {
		referencedFields.add(field);
	}

	@Override
	public IDataSet call() throws Exception {
		if((function == null) || (inputDataSet == null) || (args == null)) {
			//TODO Manage exception properly
			throw new IllegalStateException();
		}
		args.setInputDataSet(inputDataSet);
		IDataSet result = (IDataSet) function.apply(args);
		return result;
	}
	
}
