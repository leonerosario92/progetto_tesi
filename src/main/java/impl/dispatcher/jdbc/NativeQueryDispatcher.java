package impl.dispatcher.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataset.IRecordIterator;
import datasource.IRemoteDataSource;
import dispatcher.MeasurementType;
import dispatcher.QueryDispatcher;
import impl.datasource.jdbc.JDBCDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.datasource.jdbc.JDBCRecordIterator;
import impl.query.execution.ExecutionException;
import query.builder.Query;

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
			ResultSet rs = statement.executeQuery(sqlStatement);
			//TODO spostare getRecord count in una utility class
			IRecordIterator result = new JDBCRecordIterator(rs,0);
			return result;
		} catch (SQLException | JDBCDataSourceException e) {
			//TODO: Manage exception properly
			throw new RuntimeException();
		}
	}

	
	@Override
	public IRecordIterator dispatchQuery(Query query, MeasurementType measurementType) throws ExecutionException {
		switch(measurementType) {
		case EVALUATE_PERFORMANCE :
			query.setExecutionStartTime();
			IRecordIterator result = dispatchQuery(query);
			query.setExecutionEndTime();
			return result;
		case EVALUATE_MEMORY_OCCUPATION :
			query.setResultSetByteSize(0L);
			return dispatchQuery(query);
		default :
			throw new IllegalArgumentException("Unknown measurement type.");
		}
	}
}
