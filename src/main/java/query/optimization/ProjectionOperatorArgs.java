package query.optimization;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dataprovisioner.LoadingType;
import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;

public class ProjectionOperatorArgs implements IOperatorArgs{
	
	private Set<FieldDescriptor> columns;
	private LoadingType loadingType;
	
	public ProjectionOperatorArgs() {
		columns = new HashSet<>();
	}
	
	public Set<FieldDescriptor> getColumns(){
		return columns;
	}
	
	public void setColumns(Set<FieldDescriptor> fields) {
		for(FieldDescriptor column : fields) {
			columns.add(column);
		}
	}
	
	public LoadingType getLoadingType() {
		return loadingType;
	}
	
	public void setLoadingType(LoadingType loadingType) {
		this.loadingType = loadingType;
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
		
		sb.append("Loading Type = ").append(loadingType.name());
		
		return sb.toString();
	}

}
