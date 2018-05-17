package impl.dataset.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import dataset.IRecord;
import dataset.IRecordIterator;

public class JDBCRecordIterator implements IRecordIterator {

	private ResultSet resultSet;
	
	public JDBCRecordIterator(ResultSet resultSet) {
		this.resultSet = resultSet;
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
			return resultSet.isLast();
		} catch (SQLException e) {
			// TODO Manage exception properly
			throw new RuntimeException("An error occurred while retrieving data from data source");
		}
	}
	
	

}
