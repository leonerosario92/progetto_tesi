package query.execution.operator.loadmaterialized;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;

public class LoadMaterializedArgs implements IOperatorArgs {

	private Set<FieldDescriptor> columns;
	
	public LoadMaterializedArgs() {
		columns= new HashSet<>();
	}
	
	public Set<FieldDescriptor> getColumns(){
		return columns;
	}
	
	public void setColumns(Set<FieldDescriptor> fields) {
		for(FieldDescriptor column : fields) {
			columns.add(column);
		}
	}
	
	@Override
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Columns = [");
		Iterator<FieldDescriptor> it = columns.iterator();
		FieldDescriptor column;
		while(it.hasNext()) {
			column = it.next();
			sb.append(" " + column.getName());
			if(it.hasNext()){
				sb.append(" , ");
			}
		}
		sb.append(" ] ");
		
		return sb.toString();
	}

}
