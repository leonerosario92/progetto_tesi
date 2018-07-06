package query.execution.operator.orderby;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import model.FieldDescriptor;
import query.execution.operator.IOperatorArgs;

public class OrderByArgs implements IOperatorArgs {
	
	private List<FieldDescriptor> orderingSequence;
	
	public OrderByArgs() {
		orderingSequence = new ArrayList<>();
	}
	
	public List<FieldDescriptor> getOrderingSequence() {
		return orderingSequence;
	}
	
	public Set<FieldDescriptor> getColumns(){
		return Sets.newHashSet(orderingSequence);
	}

	public void setOrderingSequence(List<FieldDescriptor> orderingSequence) {
		this.orderingSequence.addAll(orderingSequence);
	}

	@Override
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("orderingSequence = [ ");
		Iterator<FieldDescriptor> it = orderingSequence.iterator();
		while(it.hasNext()) {
			sb.append( it.next().getName());
			if(it.hasNext()) {
				sb.append(" , ");
			}
		}sb.append(" ]");
		
		return sb.toString();
	}
	
	

}
