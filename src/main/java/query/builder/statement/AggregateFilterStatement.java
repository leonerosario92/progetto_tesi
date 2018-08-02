package query.builder.statement;

import java.util.HashSet;
import java.util.Set;

import model.AggregationDescriptor;
import model.FieldDescriptor;
import query.builder.LogicalOperand;
import query.builder.QueryConstants;
import query.builder.predicate.AggregateFunction;
import query.builder.predicate.FilterStatementType;

public class AggregateFilterStatement  extends FilterStatement{

	private AggregationDescriptor descriptor;
	
	
	public AggregateFilterStatement(
			AggregateFunction aggregateFunction,
			FieldDescriptor field,
			FilterStatementType filterType,
			Object rightOperand
	){
		super(filterType);
		this.setField(field);
		this.setOperand(rightOperand);
		this.descriptor = new AggregationDescriptor(field, aggregateFunction);
	}
	
	
	@Override
	public String writeSql() {
		String tableName = field.getTable().getName();
		String fieldName = field.getName();
		StringBuilder sb = new StringBuilder();
			
		sb.append(this.descriptor.toString())
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(TYPE.representation)
		.append(QueryConstants.WHITESPACE_CHAR)
		.append(rightOperand);
		
		return sb.toString(); 
	}


	public AggregationDescriptor getAggregationDescriptor() {
		return descriptor;
	}


	
}
