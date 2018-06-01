package impl.dispatcher.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataset.IRecordIterator;
import datasource.IDataSource;
import datasource.IRemoteDataSource;
import dispatcher.QueryDispatcher;
import impl.datasource.jdbc.JDBCDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.datasource.jdbc.JDBCRecordIterator;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.IQueryExecutor;

public class NativeQueryDispatcher extends QueryDispatcher {

	public NativeQueryDispatcher() {
		super();
	}

	@Override
	public IRecordIterator dispatchQuery(Query query) {
		String sqlStatement = query.writeSql();
		if(!(dataSource instanceof JDBCDataSource)) {
			//TODO: Manage exception properly
			throw new IllegalStateException();
		}
		Connection connection = (Connection) ((IRemoteDataSource)dataSource).getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			
			query.setExecutionStartTime();
			
			ResultSet rs = statement.executeQuery(sqlStatement);
			
			query.setExecutionEndTime();
			
			
			IRecordIterator result = new JDBCRecordIterator(rs);
			return result;
		} catch (SQLException | JDBCDataSourceException e) {
			//TODO: Manage exception properly
			throw new RuntimeException();
		}
	}
}
