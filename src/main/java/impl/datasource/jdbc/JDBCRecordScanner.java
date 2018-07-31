package impl.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import datasource.IRecordScanner;
import datatype.DataType;
import datatype.ITypeFactory;
import model.FieldDescriptor;

public class JDBCRecordScanner implements IRecordScanner {

	private ResultSet resultSet;
	private ResultSetMetaData metadata;
	private ITypeFactory typeFactory;
	private int recordCount;
	private int fieldsCount;
	private Map<String,Integer>nameIndexMapping;


	public JDBCRecordScanner(ResultSet resultSet, int recordCount) throws JDBCDataSourceException {
		this.recordCount = recordCount;
		this.resultSet = resultSet;
		this.typeFactory= new JDBCDataTypeFactory( );
		try {
			this.metadata = resultSet.getMetaData();
			this.fieldsCount = metadata.getColumnCount();
		} catch (SQLException e) {
			manageSqlException();
		}
		initializeColumnIndexMapping();
	}


	private void initializeColumnIndexMapping() {
		nameIndexMapping = new HashMap<>();
		for(int index=1; index<=fieldsCount; index++) {
			String tableName = getTableName(index);
			String columnName = getColumnName(index);
			
			nameIndexMapping.put(
				tableName+"_"+columnName, 
				index
			);
		}
	}


	@Override
	public int getFieldsCount() {
		return fieldsCount;
	}


	@Override
	public DataType getColumnType(int index) {
		int columntype;
		try {
			columntype = metadata.getColumnType(index);
			return typeFactory.toDataType(columntype);
		} catch (SQLException e) {
			manageSqlException();
		}
		return null;
	}


	@Override
	public String getColumnName(int index) {
		try {
			return metadata.getColumnName(index);
		} catch (SQLException e) {
			manageSqlException();
		}
		return null;
	}


	@Override
	public String getTableName(int index) {
		try {
			return metadata.getTableName(index);
		} catch (SQLException e) {
			manageSqlException();
		}
		return null;
	}
	
	
	@Override
	public Object getValueByColumnIndex(int index) {
		try {
			return resultSet.getObject(index);
		} catch (SQLException e) {
			e.printStackTrace();
			manageSqlException();
		}
		return null;
	}
	
	
	@Override
	public Object getValueByColumnDescriptor(FieldDescriptor field) {
		String columnName = field.getName();
		try {
			return resultSet.getObject(columnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			manageSqlException();
		}
		return columnName;
	}
	
	
	@Override
	public boolean next() {
		try {
			boolean result = resultSet.next();
			if( ! result) {
				resultSet.close();
			}
			return result;
			
		} catch (SQLException e) {
			manageSqlException();
			return false;
		}
	}
	
	
	@Override
	public int getRecordCount() {
		return recordCount;
	}
	
	
	@Override
	public void resetToFirstRecord() {
		try {
			resultSet.beforeFirst();
		} catch (SQLException e) {
			manageSqlException();
		}
	}
	
	
	private void manageSqlException() {
		// TODO Manage exception properly
		throw new RuntimeException("An error occurred while retrieving data from data source");
	}


	@Override
	public int getColumnIndex(FieldDescriptor field) {
		String columnName = field.getName();
		for(int index = 1; index <= fieldsCount; index ++) {
			if(getColumnName(index).equals(columnName)) {
				return index;
			}
		}
		//TODO Manage exception properly
		throw new IllegalArgumentException("Attempt to retrieve index of unknown column.");
	}


	@Override
	public Object[] getCurrentRecord() {
		Object[] result = new Object[fieldsCount];
		for(int i=1; i<=fieldsCount; i++) {
			try {
				result[i-1] = resultSet.getObject(i);
			} catch (SQLException e) {
				manageSqlException();
			}
		}
		return result;
	}


	@Override
	public String getColumnId(int index) {
		String tableName = getTableName(index);
		String columnName = getColumnName(index);
		return tableName+"_"+columnName;
	}


	@Override
	public Object getValueByColumnID(String columnId) {
		if(! (nameIndexMapping.containsKey(columnId))) {
			throw new IllegalArgumentException("Attempt to retrieve value from an unknown column");
		}
		int index = nameIndexMapping.get(columnId);
		return getValueByColumnIndex(index);
	}
	
	
	public Map<String, Integer> getColumnIndexMapping() {
		return nameIndexMapping;
	}

	public void setColumnIndexMapping(Map<String, Integer> columnIndexMapping) {
		this.nameIndexMapping = columnIndexMapping;
	}


	@Override
	public Map<String, Integer> getNameIndexMapping() {
		return nameIndexMapping;
	}

}
