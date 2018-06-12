package query.execution;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataset.IDataSet;
import impl.query.execution.ExecutionException;
import model.FieldDescriptor;
import model.TableDescriptor;


public class ExecutionPlan {
	
	private IExecutable rootExecutable;
	
	public ExecutionPlan(IExecutable rootExecutable) {
		this.rootExecutable = rootExecutable;
	}

	public IExecutable getRootExecutable() {
		return rootExecutable;
	}
	
}
