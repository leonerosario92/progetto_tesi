package query.filter;

import java.util.function.Predicate;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import model.IField;
import query.function.Function2;
import query.function.Function3;


public interface IFilterFunction 
	extends Function2<IDataContext, IFilterQueryParams, IDataIterator>{  }
 