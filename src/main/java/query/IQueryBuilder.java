package query;

import java.util.Set;
import java.util.function.Predicate;

import dataIterator.IDataIterator;
import model.IField;
import model.ITable;

public interface IQueryBuilder {
	
	//Costruita a partire da un dataSource(DataSet se implementato)
	
	public void filter(ITable table, IField field, Predicate<?> condition);

	public void project(ITable table, IField field);
	
	public Set<IQueryNode> getQueryPlan();
	
	public IDataIterator execute();
}
