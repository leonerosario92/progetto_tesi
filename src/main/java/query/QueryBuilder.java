package query;

import java.util.Set;

public interface QueryBuilder {
	
	//Costruita a partire da un dataSource(DataSet se implementato)
	
	//Verifica la validità della query nel contesto del dataset di cui sopra
	public void checkValidity();
	
	public QueryBuilder filter(String fieldName, Object value, Class type )
	
	
	public Set<QueryNode> getQueryPlan();
}
