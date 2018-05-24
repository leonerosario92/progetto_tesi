package query.execution.operator.filterscan;
import java.util.ArrayList;
import java.util.List;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.builder.statement.FilterStatement;
import query.execution.operator.IOperatorArgs;

public class FilterScanArgs implements IOperatorArgs {
	
		private List<FilterStatement> statements;
		private FieldDescriptor field;
		private IDataSet inputDataSet;
		
		public FilterScanArgs() {
			statements = new ArrayList<>();
		}


		public void setStatements(List<FilterStatement> statements) {
			statements.addAll(statements);
		}
		
		public  List<FilterStatement> getStatements() {
			return statements;
		}


		public FieldDescriptor getField() {
			return field;
		}


		public void setField(FieldDescriptor field) {
			this.field = field;
		}


		@Override
		public void setInputDataSet(IDataSet inputSet) {
			this.inputDataSet = inputSet;
		}


		@Override
		public IDataSet getInputDataSet() {
			return inputDataSet;
		}
		
	}