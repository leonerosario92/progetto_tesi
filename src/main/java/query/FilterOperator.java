package query;
import java.util.function.Predicate;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import model.IField;
import query.function.FilterFunction;

public  class FilterOperator extends AbstractRelOperator {
	
	FilterFunction function;
	
	public FilterOperator(FilterFunction function) {
		super( RelOperatorType.FILTER);
		this.function = function;
	}

	public IDataIterator exec(Object... args) {
		
		checkArgs(args);
		
		IDataContext context =  (IDataContext) args[0];
		IField field = (IField) args[1];
		Predicate<?> condition = (Predicate<?>) args[2];
		
		IDataIterator result = function.apply(context, field, condition);
	}

	

	

	
	
}