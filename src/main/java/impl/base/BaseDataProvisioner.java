package impl.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataprovisioner.DataProvisioner;
import dataset.IDataSet;
import dataset.ILayoutManager;
import dataset.IRecordIterator;
import datasource.DataSourceException;
import datasource.IDataSource;
import datasource.IRecordScanner;
import model.FieldDescriptor;
import model.TableDescriptor;
import query.builder.statement.CFNode;
import utils.RecordEvaluator;

public class BaseDataProvisioner extends DataProvisioner  {
	
	
	public BaseDataProvisioner() {
		super();
	}

	
//	@Override
//	public IDataSet<?> loadSingleColumnDataset(FieldDescriptor field) throws DataSourceException {
//		
//		// Here will be performed the search of requested data in the cache
//		
//		//Retrieve from dataSource the data that has not been found in the cache
//		
//		IRecordScanner it = 
//				dataSource.getTableProjection(field.getTable(), field);
//		IDataSet<?> result = layoutManager.buildColumnarDataSet(it);
//		
//		//Merge data found in cache with data retrieved from dataSource 
//		
//		return result;
//	}
//
//	
//	@Override
//	public IDataSet<?> loadColumnarDataSet(Set<FieldDescriptor> fields) throws DataSourceException {
//		
//		// Here will be performed the search of requested data in the cache
//		
//		//Retrieve from dataSource the data that has not been found in the cache
//		
//		Map <TableDescriptor, Set<FieldDescriptor>> groupedFields = fieldsByTable(fields);
//		IRecordScanner rs = null;
//		if(groupedFields.keySet().size() == 1) {
//			TableDescriptor table =
//					groupedFields.entrySet().iterator().next().getKey();
//			rs = dataSource.getTableProjection(table, fields.toArray(new FieldDescriptor[fields.size()] ));
//		}else {
//			//TODO manage load from multiple Table if required
//		}
//		
//		IDataSet result = layoutManager.buildColumnarDataSet(rs);
//				
//		//Merge data found in cache with data retrieved from dataSource 
//		
//		return result;
//	}


	@Override
	public IDataSet loadMaterializedDataSet(Set<FieldDescriptor> columns) throws DataSourceException {
		Map <TableDescriptor, Set<FieldDescriptor>> groupedFields = fieldsByTable(columns);
		IRecordScanner rs = null;
		if(groupedFields.keySet().size() == 1) {
			TableDescriptor table =
					groupedFields.entrySet().iterator().next().getKey();
			rs = dataSource.getTableProjection(table, columns.toArray(new FieldDescriptor[columns.size()] ));
		}else {
			//TODO manage load from multiple Table if required
		}
		
		IDataSet result = layoutManager.buildMaterializedDataSet(rs);
		return result;
	}

	
	@Override
	public IDataSet loadFilteredMaterializedDataSet(Set<FieldDescriptor> columns, Set<CFNode> filterStatements)
			throws DataSourceException {

		Map <TableDescriptor, Set<FieldDescriptor>> groupedFields = fieldsByTable(columns);
		IRecordScanner rs = null;
		if(groupedFields.keySet().size() == 1) {
			TableDescriptor table =
					groupedFields.entrySet().iterator().next().getKey();
			rs = dataSource.getTableProjection(table, columns.toArray(new FieldDescriptor[columns.size()] ));
		}else {
			//TODO manage load from multiple Table if required
		}
		
		/*===TODO differentiate mapping for recordScanner and recordIterator===*/
		Map<String,Integer> mapping = new HashMap<>();
		for ( Entry<String, Integer> entry : rs.getNameIndexMapping().entrySet()) {
			mapping.put(entry.getKey(), entry.getValue()-1);
		}
		/*=====================================================================*/
		
		RecordEvaluator evaluator = new RecordEvaluator(mapping, filterStatements);
		IDataSet result = layoutManager.buildMaterializedDataSet(rs,evaluator);
		return result;
	}
	
	
	@Override
	public StreamPipeline loadStreamedDataSet(Set<FieldDescriptor> columns) throws DataSourceException {
		Map <TableDescriptor, Set<FieldDescriptor>> groupedFields = fieldsByTable(columns);
		IRecordScanner rs = null;
		if(groupedFields.keySet().size() == 1) {
			TableDescriptor table =
					groupedFields.entrySet().iterator().next().getKey();
			rs = dataSource.getTableProjection(table, columns.toArray(new FieldDescriptor[columns.size()] ));
		}else {
			//TODO manage load from multiple Table if required
		}
		
		StreamPipeline result = layoutManager.buildStreamedDataSet(rs);
		return result;
	}
	
	
	private HashMap<TableDescriptor, Set<FieldDescriptor>> fieldsByTable(Set<FieldDescriptor> fields)
	{
		HashMap<TableDescriptor,Set<FieldDescriptor>> groupedFields = new HashMap<> ();
		for(FieldDescriptor field : fields) {
			TableDescriptor table = field.getTable();
			if(groupedFields.containsKey(table)) {
				groupedFields.get(table).add(field);
			}else {
				HashSet<FieldDescriptor> s = new HashSet<>();
				s.add(field);
				groupedFields.put(table,s);
			}
		}
		return groupedFields;
	}


}
