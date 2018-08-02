package query.builder.statement;

import model.AggregationDescriptor;
import model.FieldDescriptor;
import model.IDescriptor;

public class ProjectionStatement {

	private IDescriptor descriptor;
	private boolean isAggregate = false;
	private boolean toShow;
	
	
	public ProjectionStatement(IDescriptor descriptor, boolean toShow) {
		this.descriptor = descriptor;
		this.toShow = toShow;
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
	
	
	public boolean toSHow() {
		return this.toShow;
	}


	public IDescriptor getField() {
		return descriptor;
	}
}
