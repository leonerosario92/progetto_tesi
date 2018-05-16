package query.builder;

import model.FieldDescriptor;
import model.TableDescriptor;

public class JoinDescriptor {
	
	private TableDescriptor dimension;
	private FieldDescriptor factKey;
	private FieldDescriptor dimensionKey;
	
	
	public JoinDescriptor(TableDescriptor dimension, FieldDescriptor factKey, FieldDescriptor dimensionKey) {
		this.dimension = dimension;
		this.factKey = factKey;
		this.dimensionKey = dimensionKey;
	}


	public TableDescriptor getDimension() {
		return dimension;
	}


	public FieldDescriptor getFactKey() {
		return factKey;
	}


	public FieldDescriptor getDimensionKey() {
		return dimensionKey;
	}

	
}
