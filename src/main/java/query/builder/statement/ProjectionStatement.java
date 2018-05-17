package query.builder.statement;

import model.FieldDescriptor;

public class ProjectionStatement {

	private FieldDescriptor field;
	
	public ProjectionStatement(FieldDescriptor field) {
		this.field = field;
	}

	public FieldDescriptor getField() {
		return field;
	}
}
