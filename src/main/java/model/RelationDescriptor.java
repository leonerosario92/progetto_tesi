package model;

public class RelationDescriptor {
	
	private TableDescriptor factTable;
	private FieldDescriptor factKey;
	private TableDescriptor dimension;
	private FieldDescriptor dimensionKey;

	
	public RelationDescriptor(TableDescriptor factTable, FieldDescriptor factKey, TableDescriptor dimension,
			FieldDescriptor dimensionKey) {
		super();
		this.factTable = factTable;
		this.factKey = factKey;
		this.dimension = dimension;
		this.dimensionKey = dimensionKey;
	}


	public TableDescriptor getFactTable() {
		return factTable;
	}


	public FieldDescriptor getFactKey() {
		return factKey;
	}


	public TableDescriptor getDimension() {
		return dimension;
	}


	public FieldDescriptor getDimensionKey() {
		return dimensionKey;
	}
	
	
	
}
