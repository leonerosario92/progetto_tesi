package model;

import query.builder.predicate.AggregateFunction;

public class AggregationDescriptor implements IDescriptor{
	
	private FieldDescriptor field;
	private AggregateFunction function;
	
	public AggregationDescriptor(FieldDescriptor field, AggregateFunction function) {
		this.field = field;
		this.function = function;
	}

	public FieldDescriptor getField() {
		return field;
	}

	public AggregateFunction getFunction() {
		return function;
	}
	
	public String getKey () {
		return function.getRepresentation() + "_"+ field.getKey();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof AggregationDescriptor))return false;
	    AggregationDescriptor otherFieldDescriptor = (AggregationDescriptor)other;
		String otherKey = otherFieldDescriptor.getKey();
		String thisKey = getKey();	
		return thisKey.equals(otherKey);
	}
	
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}
	
	@Override
	public String toString() {
		return function.getRepresentation() + "(" + field.toString() + ")";
	}

	@Override
	public String getName() {
		return function.getRepresentation() + "("+ field.getKey() + ")";
	}

	@Override
	public TableDescriptor getTable() {
		return field.getTable();
	}
}
