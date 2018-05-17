package query.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dataset.IDataSet;

public class ExecutionSequence implements IExecutableSet{

	private List<IExecutableSet> executableSets;
	
	@Override
	public IDataSet execute(IQueryExecutor executor,IDataSet...inputSets) {
		if(inputSets.length > 1) {
			//TODO: manage exception properly
			throw new IllegalArgumentException();
		}
		IDataSet inputSet = inputSets[0];
		for(IExecutableSet exSet : executableSets) {
			inputSet = exSet.execute(executor, inputSet);
		}
		return inputSet;
	}


	

	
}
