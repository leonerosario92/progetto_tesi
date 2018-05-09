package query;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

import dataIterator.IDataIterator;
import datacontext.DataContext;
import model.IField;

public interface IQueryProvider {
	
	public void setOperator (IRelOperator operator);
	
	public IDataIterator filter (DataContext context, IField field, Predicate condition );
	
}
