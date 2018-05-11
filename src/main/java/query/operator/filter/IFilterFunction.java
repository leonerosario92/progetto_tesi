package query.operator.filter;

import java.util.function.Predicate;

import context.IContext;
import dataset.IDataIterator;
import dataset.IEntity;
import model.IFieldDescriptor;
import query.function.Function2;
import query.function.Function3;


public interface IFilterFunction 
	extends Function2<IEntity, IFilterQueryParams, IEntity>{  }
 