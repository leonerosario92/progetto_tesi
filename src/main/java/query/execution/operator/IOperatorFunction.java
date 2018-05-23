package query.execution.operator;

import java.util.function.Function;

import dataset.IDataSet;
import dataset.IEntity;

@FunctionalInterface
public interface IOperatorFunction <I,R> extends Function<I,R>{
	
}
