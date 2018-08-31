package query.execution.operator.loadstream;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;

public class LoadStreamArgs implements IOperatorArgs{

	private Set<FieldDescriptor> columns;
	
	
	public LoadStreamArgs() {
		this.columns = new HashSet<>();
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
		Iterator<FieldDescriptor> fieldIt = columns.iterator();
		FieldDescriptor column;
		while(fieldIt.hasNext()) {
			column = fieldIt.next();
			sb.append(" " + column.getName());
			if(fieldIt.hasNext()){
				sb.append(" , ");
			}
		}
		sb.append(" ] ");
		return sb.toString();
	}

}
