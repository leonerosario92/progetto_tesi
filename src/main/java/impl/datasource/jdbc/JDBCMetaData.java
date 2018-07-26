package impl.datasource.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import datatype.DataType;
import datatype.ITypeFactory;
import model.FieldDescriptor;
import model.IMetaData;
import model.RelationDescriptor;
import model.TableDescriptor;

public class JDBCMetaData implements IMetaData {
	
	private ArrayList<TableDescriptor> tables;
	private ArrayList<RelationDescriptor> relations;
	private Connection connection;
	private ITypeFactory typeFactory;
	
	public JDBCMetaData(Connection connection) throws JDBCDataSourceException {
		
		this.connection = connection;
		this.tables= new ArrayList<TableDescriptor>();
		this.typeFactory = new JDBCDataTypeFactory();
		
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			
			ResultSet tablesMetaData = metadata.getTables(null, null, null, null);
			
			while(tablesMetaData.next()) {
				String tableName = tablesMetaData.getString("TABLE_NAME");
				TableDescriptor newTable = new TableDescriptor(tableName);
				Set<String> primaryKeys = new HashSet<String>();

				ArrayList<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
				ResultSet columns = metadata.getColumns(null, null, tableName, null);
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					int columnTypeIndex = columns.getInt("DATA_TYPE");
					DataType columnType = typeFactory.toDataType(columnTypeIndex);
					boolean isKey = primaryKeys.contains(columnName);
					
					FieldDescriptor newField = new FieldDescriptor(newTable, columnName, columnType,isKey);
					fields.add(newField);
					newTable.addFields(fields);
				}
				
				newTable.setRecordCount(getRecordCount(newTable));
				
				tables.add(newTable);
			}
	
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
	
	
	private Integer getRecordCount(TableDescriptor table) throws SQLException {
		String query = "select count(1) from " +table.getName();
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		
		Integer count = null;
		if(result.next()) {
			 count = result.getInt(1);
		}
		
		return count;
	}

	


	
}
