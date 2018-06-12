package query.execution.operator.filteroncolumn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.FieldDescriptor;
import query.builder.statement.FilterStatement;
import query.execution.operator.IOperatorArgs;

public class FilterOnColumnArgs implements IOperatorArgs {
	
		private Set<FilterStatement> statements;
		private FieldDescriptor field;
		
		
		public FilterOnColumnArgs() {
			statements = new HashSet<>();
		}


		public void setStatements(Set<FilterStatement> set) {
			this.statements.addAll(set);
		}
		
		public  Set<FilterStatement> getStatements() {
			return statements;
		}


		public FieldDescriptor getField() {
			return field;
		}


		public void setField(FieldDescriptor field) {
			this.field = field;
		}
		
	}