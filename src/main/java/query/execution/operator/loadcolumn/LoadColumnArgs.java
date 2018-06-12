package query.execution.operator.loadcolumn;

import dataprovisioner.LoadingType;
import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;

public class LoadColumnArgs implements IOperatorArgs {
	
	private FieldDescriptor column;
	private LoadingType loadingType;
	
	public FieldDescriptor getColumn() {
		return column;
	}
	public void setColumn(FieldDescriptor column) {
		this.column = column;
	}
	public LoadingType getLoadingType() {
		return loadingType;
	}
	public void setLoadingType(LoadingType loadingType) {
		this.loadingType = loadingType;
	}
	
}
