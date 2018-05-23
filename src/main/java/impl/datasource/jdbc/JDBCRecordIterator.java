package impl.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import dataset.IRecord;
import dataset.IRecordIterator;

public class JDBCRecordIterator implements IRecordIterator {

	private ResultSet resultSet;
	private ResultSetMetaData metadata;
	
	public JDBCRecordIterator(ResultSet resultSet) throws JDBCDataSourceException {
		this.resultSet = resultSet;
		try {
			this.metadata = resultSet.getMetaData();
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new JDBCDataSourceException();
		}
	}

	
	@Override
	public IRecord getNextRecord()  {
		try {
			return new JDBCRecord(resultSet.next());
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
	}

	
	@Override
	public boolean hasNext() {
		try {
			return (!resultSet.isLast());
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
	}


	@Override
	public int getFieldsCount() {
		try {
			return metadata.getColumnCount();
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
	}


	@Override
	public Class<?> getColumnType(int index) {
		int columntype;
		try {
			columntype = metadata.getColumnType(index);
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
		return JDBCDataTypeFactory.toJavaClass(columntype);
	}


	@Override
	public String getColumnName(int index) {
		try {
			return metadata.getColumnName(index);
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
	}
	

}
