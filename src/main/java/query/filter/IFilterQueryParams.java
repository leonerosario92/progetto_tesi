package query.filter;

import java.util.function.Predicate;

import model.IField;
import model.ITable;
import query.IQueryParams;

public interface IFilterQueryParams extends IQueryParams {
	
	public ITable getTable();
	public IField getField();
	public Predicate<?> getPredicate();
	
}