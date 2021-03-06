package query.execution.operator.streamedorderby;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import model.FieldDescriptor;
import model.IDescriptor;
import query.execution.operator.IOperatorArgs;

public class StreamedOrderByArgs implements IOperatorArgs {

	private List<FieldDescriptor> orderingSequence;
	
	public StreamedOrderByArgs() {
		orderingSequence = new ArrayList<>();
	}
	
	public List<FieldDescriptor> getOrderingSequence() {
		return orderingSequence;
	}
	
	public Set<IDescriptor> getColumns(){
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
