package query.function;

import java.util.function.Predicate;

import dataIterator.IDataIterator;
import datacontext.IDataContext;
import model.IField;


public interface FilterFunction 
	extends Function3<IDataContext, IField, Predicate<?>, IDataIterator>{  }
 