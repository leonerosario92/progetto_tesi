package impl.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import dataset.IRecordIterator;
import datatype.DataType;
import datatype.ITypeFactory;

public class JDBCRecordIterator implements IRecordIterator {

	private ResultSet resultSet;
	private ResultSetMetaData metadata;
	private ITypeFactory typeFactory;
	private int recordCount;

	
	public JDBCRecordIterator(ResultSet resultSet, int recordCount) throws JDBCDataSourceException {
		this.recordCount = recordCount;
		this.resultSet = resultSet;
		this.typeFactory= new JDBCDataTypeFactory( );
		try {
			this.metadata = resultSet.getMetaData();
		} catch (SQLException e) {
			manageSqlException();
		}
	}


	@Override
	public boolean hasNext() {
		try {
			return (!resultSet.isLast());
		} catch (SQLException e) {
			manageSqlException();
		}
		return false;
	}


	@Override
	public int getFieldsCount() {
		try {
			return metadata.getColumnCount();
		} catch (SQLException e) {
			manageSqlException();
		}
		return 0;
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
	public Object getValueByColumnName(String columnName) {
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
	public void next() {
		try {
			resultSet.next();
		} catch (SQLException e) {
			manageSqlException();
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


}
