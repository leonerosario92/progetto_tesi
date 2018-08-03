package query.builder;

import java.util.Set;

import com.google.common.collect.Sets;

import context.Context;
import model.FieldDescriptor;
import query.builder.clause.GroupByClause;
import query.builder.clause.ProjectionClause;

public class OrderByBuilder {

	private Context context;
	private Query query;
	
	
	public OrderByBuilder(Context context, Query query) {
		
		this.context = context;
		this.query = query;
	}
	
	
	public Query getQuery() {
		GroupByClause groupByClause = query.getGroupByClause();
		if(! groupByClause.getGroupingSequence().isEmpty()) {
			ProjectionClause projectionClause = query.getProjectionClause();
			Set<FieldDescriptor> nonAggregateFields = 
				Sets.difference(
						projectionClause.getProjectedFields(),
						projectionClause.getAggregateFields()
				);
			for(FieldDescriptor field : nonAggregateFields) {
				if(! groupByClause.getGroupingSequence().contains(field)) {
					//TODO : Manage exception properly
					throw new IllegalArgumentException(
							"Wrong Syntax : Non-aggregate field " + field.getName() + " cannot be selected for projection.");
				}
			}
		}
		else {
			if(! query.getProjectionClause().getAggregations().isEmpty()) {
				throw new IllegalArgumentException("Non-aggregate queries cannot project aggregate fields.");
			}
	}
		
		return query;
	}
	
}
