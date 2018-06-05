package impl.query.execution.operator.filterscan;


import dataset.IDataSet;
import model.FieldDescriptor;
import query.builder.predicate.FilterStatementType;
import query.builder.statement.FilterStatement;
import query.execution.operator.filterscan.FilterScanArgs;
import query.execution.operator.filterscan.FilterScanFunction;

public class FilterScanImpl extends FilterScanFunction{

	@Override
	public IDataSet apply(FilterScanArgs args) {
		
		IDataSet inputSet = args.getInputDataSet();
		FieldDescriptor field = args.getField();
		
		for(FilterStatement statement : args.getStatements()) {
			FilterStatementType type = statement.getFilterType();
			
		}
		return inputSet;
		
	}

	

}
