package impl.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import context.DataType;
import dataset.IRecordIterator;

public class JDBCRecordIterator implements IRecordIterator {

	private ResultSet resultSet;
	private ResultSetMetaData metadata;
	private int recordCount;

	
	public JDBCRecordIterator(ResultSet resultSet, int recordCount) throws JDBCDataSourceException {
		this.recordCount = recordCount;
		this.resultSet = resultSet;
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
			return JDBCDataTypeFactory.toDataType(columntype);
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
			return metadata.getColumnName(index);
		} catch (SQLException e) {
			manageSqlException();
		}
		return null;
	}
	
	
	@Override
	public Object getValueByIndex(int index) {
		try {
			return resultSet.getObject(index);
		} catch (SQLException e) {
			e.printStackTrace();
			manageSqlException();
		}
		return null;
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
	
	
	private void manageSqlException() {
		// TODO Manage exception properly
		throw new RuntimeException("An error occurred while retrieving data from data source");
	}

}
