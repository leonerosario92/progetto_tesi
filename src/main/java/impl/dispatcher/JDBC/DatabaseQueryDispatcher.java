package impl.dispatcher.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import context.Context;
import dataset.IRecordIterator;
import datasource.IDataSource;
import datasource.IRemoteDataSource;
import impl.dataset.jdbc.JDBCDataSource;
import impl.dataset.jdbc.JDBCRecordIterator;
import query.IQueryDispatcher;
import query.builder.Query;

public class DatabaseQueryDispatcher implements IQueryDispatcher {

	@Override
	public IRecordIterator dispatchQuery(Query query, Context context) {
		String sqlStatement = query.writeSql();
		IDataSource dataSource = context.getDataSource();
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
		} catch (SQLException e) {
			//TODO: Manage exception properly
			throw new RuntimeException();
		}
	}
}
