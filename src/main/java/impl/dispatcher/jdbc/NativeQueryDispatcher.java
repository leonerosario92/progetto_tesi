package impl.dispatcher.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataset.IRecordIterator;
import datasource.IDataSource;
import datasource.IRemoteDataSource;
import dispatcher.AbstractQueryDispatcher;
import impl.datasource.jdbc.JDBCDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.datasource.jdbc.JDBCRecordIterator;
import query.IQueryPlanner;
import query.builder.Query;
import query.execution.IQueryExecutor;

public class NativeQueryDispatcher extends AbstractQueryDispatcher {

	public NativeQueryDispatcher(IDataSource dataSource, IQueryPlanner planner, IQueryExecutor executor) {
		super(dataSource, planner, executor);
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
			ResultSet rs = statement.executeQuery(sqlStatement);
			IRecordIterator result = new JDBCRecordIterator(rs);
			return result;
		} catch (SQLException | JDBCDataSourceException e) {
			//TODO: Manage exception properly
			throw new RuntimeException();
		}
	}
}
