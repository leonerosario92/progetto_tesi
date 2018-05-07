package impl.jdbc.relational;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import datasource.IDataSource;
import model.Field;
import model.IMetaData;
import model.IRelation;
import model.ITable;
import model.Table;

public class JDBCMetadata implements IMetaData {
	
	private HashSet<ITable> tablesSet;
	private HashSet<IRelation> relationsSet;
	private IDataSource dataSource;
	
	public JDBCMetadata(JDBCDataSource dataSource) {
		
		this.dataSource = dataSource;
		
		tablesSet = new HashSet<ITable>();
		
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			
			ResultSet tablesMetaData = metadata.getTables(null, null, null, null);
			
			while(tablesMetaData.next()) {
				String tableName = tablesMetaData.getString("TABLE_NAME");
				Table newTable = new Table(tableName);
				Set<String> tableKeys = new HashSet<String>();
				ResultSet keys = metadata.getPrimaryKeys(null, null, tableName);
				
				while(keys.next()) {
					tableKeys.add(keys.getString("COLUMN_NAME"));
				}
				
				ResultSet columns = metadata.getColumns(null, null, tableName, null);
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					int columnType = columns.getInt("DATA_TYPE ");
					
					Field newField = new Field(newTable, columnName, );
				}
			}
			
		} catch (SQLException e) {
			//TODO  manage exception properly
		}
	}

	public Set<ITable> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	public ITable getTable(String tableId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IRelation> getRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IRelation> getRelations(String srcTableId, String dstTableId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
