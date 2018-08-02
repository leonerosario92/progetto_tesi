package query.builder.statement;

import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;

public class ProjectionStatement {

	private IDescriptor descriptor;
	private boolean isAggregate = false;
	
	
	public ProjectionStatement(IDescriptor descriptor) {
		this.descriptor = descriptor;
		if(descriptor instanceof AggregationDescriptor) {
			isAggregate = true;
		}
	}
	
	
	public IDescriptor getDescriptor() {
		return descriptor;
	}
	
	
	public boolean isAggregate() {
		return isAggregate;
	}


	public IDescriptor getField() {
		return descriptor;
	}
}
