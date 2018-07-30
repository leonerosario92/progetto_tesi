package impl.query.execution.operator.loadmaterialized;

import java.util.Set;

import dataprovisioner.IDataProvisioner;
import dataset.IDataSet;
import datasource.DataSourceException;
import model.FieldDescriptor;
import query.builder.statement.CFNode;
import query.execution.operator.loadmaterialized.LoadMaterializedArgs;
import query.execution.operator.loadmaterialized.LoadMaterializedFunction;
import utils.RecordEvaluator;

public class LoadMaterializedImpl extends LoadMaterializedFunction{

	@Override
	public IDataSet apply(IDataProvisioner provisioner, LoadMaterializedArgs args) throws DataSourceException {
		Set<FieldDescriptor> columns = args.getColumns();
		Set<CFNode> filterStatements = args.getFilterStatements();
		if(filterStatements.isEmpty()) {
			return provisioner.loadMaterializedDataSet(columns);
		}else {
			return provisioner.loadFilteredMaterializedDataSet(columns,filterStatements);
		}
	}

}
