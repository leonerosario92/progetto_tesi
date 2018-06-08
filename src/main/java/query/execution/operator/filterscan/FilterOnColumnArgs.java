package query.execution.operator.filterscan;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import dataset.IDataSet;
import model.FieldDescriptor;
import query.builder.statement.FilterStatement;
import query.execution.operator.IOperatorArgs;

public class FilterOnColumnArgs implements IOperatorArgs {
	
		private List<FilterStatement> statements;
		private FieldDescriptor field;
		private IDataSet inputDataSet;
		private BitSet validityBitSet;
		
		public FilterOnColumnArgs() {
			statements = new ArrayList<>();
		}


		public void setStatements(List<FilterStatement> statements) {
			this.statements.addAll(statements);
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
		

		public BitSet getValidityBitSet() {
			return validityBitSet;
		}


		public void setValidityBitSet(BitSet validityBitSet) {
			this.validityBitSet = validityBitSet;
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