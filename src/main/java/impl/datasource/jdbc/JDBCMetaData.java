package impl.datasource.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import context.DataType;
import model.FieldDescriptor;
import model.IMetaData;
import model.RelationDescriptor;
import model.TableDescriptor;

public class JDBCMetaData implements IMetaData {
	
	private ArrayList<TableDescriptor> tables;
	private ArrayList<RelationDescriptor> relations;
	private Connection connection;
	
	public JDBCMetaData(Connection connection) throws JDBCDataSourceException {
		
		this.connection = connection;
		this.tables= new ArrayList<TableDescriptor>();
		
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			
			ResultSet tablesMetaData = metadata.getTables(null, null, null, null);
			
			while(tablesMetaData.next()) {
				String tableName = tablesMetaData.getString("TABLE_NAME");
				TableDescriptor newTable = new TableDescriptor(tableName);
				Set<String> primaryKeys = new HashSet<String>();
//				ResultSet pKeys = metadata.getPrimaryKeys(null, null, tableName);
				
//				while(pKeys.next()) {
//					primaryKeys.add(pKeys.getString("COLUMN_NAME"));
//				}
				ArrayList<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
				ResultSet columns = metadata.getColumns(null, null, tableName, null);
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					int columnTypeIndex = columns.getInt("DATA_TYPE");
					DataType columnType = JDBCDataTypeFactory.toDataType(columnTypeIndex);
					boolean isKey = primaryKeys.contains(columnName);
					FieldDescriptor newField = new FieldDescriptor(newTable, columnName, columnType,isKey);
					fields.add(newField);
					newTable.addFields(fields);
				}
				tables.add(newTable);
			}
		//TODO Add relations to neewTable
		} catch (SQLException e) {
			throw new JDBCDataSourceException("Error while building metaData"
					+ "caused by " + e.getMessage());
		}
	}

	public Iterable<TableDescriptor> getTables() {
		return tables;
	}

	public TableDescriptor getTable(String tableId) {
		for(TableDescriptor table : tables) {
			if(table.getName().equals(tableId)) {
				return table;
			}
		}
		return null;
	}

	public Iterable<RelationDescriptor> getAllRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<RelationDescriptor> getRelations(String tableId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<RelationDescriptor> getRelations(String srcTableId, String dstTableId) {
		// TODO Auto-generated method stub
		return null;
	}


	
}