package impl.jdbc.mysql.nocache;

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

public class JDBCMetaData implements IMetaData {
	
	private ArrayList<ITable> tables;
	private ArrayList<IRelation> relations;
	private Connection connection;
	
	public JDBCMetaData(Connection connection) {
		
		this.connection = connection;
		
		tables= new ArrayList<ITable>();
		
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			
			ResultSet tablesMetaData = metadata.getTables(null, null, null, null);
			
			while(tablesMetaData.next()) {
				String tableName = tablesMetaData.getString("TABLE_NAME");
				Table newTable = new Table(tableName);
				Set<String> primaryKeys = new HashSet<String>();
				ResultSet pKeys = metadata.getPrimaryKeys(null, null, tableName);
				
				while(pKeys.next()) {
					primaryKeys.add(pKeys.getString("COLUMN_NAME"));
				}
				ArrayList<Field> fields = new ArrayList<Field>();
				ResultSet columns = metadata.getColumns(null, null, tableName, null);
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					int columnTypeIndex = columns.getInt("DATA_TYPE ");
					Class <?> columnType = JDBCDataTypeFactory.toJavaClass(columnTypeIndex);
					boolean isKey = primaryKeys.contains(columnName);
					Field newField = new Field(newTable, columnName, columnType,isKey);
				}
				newTable.addFields(fields);
			}
		//TODO Add relations to neewTable
		} catch (SQLException e) {
			//TODO  manage exception properly
		}
	}

	public Iterable<ITable> getTables() {
		return tables;
	}

	public ITable getTable(String tableId) {
		for(ITable table : tables) {
			if(table.getName().equals(tableId)) {
				return table;
			}
		}
		return null;
	}

	public Iterable<IRelation> getAllRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<IRelation> getRelations(String tableId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<IRelation> getRelations(String srcTableId, String dstTableId) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
