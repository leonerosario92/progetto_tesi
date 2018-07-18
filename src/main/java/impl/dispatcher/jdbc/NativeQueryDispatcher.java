package impl.dispatcher.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import com.google.common.collect.Maps;

import dataset.IRecordIterator;
import datasource.IRecordScanner;
import datasource.IRemoteDataSource;
import dispatcher.MeasurementType;
import dispatcher.QueryDispatcher;
import impl.datasource.jdbc.JDBCDataSource;
import impl.datasource.jdbc.JDBCDataSourceException;
import impl.datasource.jdbc.JDBCRecordScanner;
import model.FieldDescriptor;
import query.builder.Query;
import query.execution.QueryExecutionException;

public class NativeQueryDispatcher extends QueryDispatcher {

	
	public NativeQueryDispatcher() {
		super();
	}

	
	@Override
	public IRecordScanner dispatchQuery(Query query) throws QueryExecutionException {
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
			
			//TODO Create getRecordCount() utility instead of passing 0
			IRecordScanner result = new JDBCRecordScanner(rs,0);

			
			/*This is needed due to the fact that if the source table is a view, the ResultSet
			 * returned by JDBC will refer to the single underlying tables, therefore is not possible
			 * to correlate the result of the "native" execution with the one returned by the solution.
			 */
			fixNameIndexMapping(query, result);
			
			
			return result;
		} catch (SQLException | JDBCDataSourceException e) {
			throw new QueryExecutionException("An Exception occurred while executing query on native data source : "+ e.getMessage());
		}
	}

	
	private void fixNameIndexMapping(Query query, IRecordScanner result) {
		Set<FieldDescriptor> referencedFields = query.getProjectionClause().getReferencedFields();
		Map <String,Integer> fixedMapping = Maps.newHashMap();
		for(int i=1; i<=result.getFieldsCount(); i++) {
			String columnName = result.getColumnName(i);
			for(FieldDescriptor field : referencedFields) {
				if (field.getName().equals(columnName)) {
					fixedMapping.put(field.getKey(),i);
					break;
				}
			}
		}
		((JDBCRecordScanner)result).setColumnIndexMapping(fixedMapping);
	}


	@Override
	public IRecordScanner dispatchQuery(Query query, MeasurementType measurementType) throws QueryExecutionException {
		switch(measurementType) {
		case EVALUATE_EXECUTION_TIME :
			long executionStartTime = System.nanoTime();
			IRecordScanner result = dispatchQuery(query);
			long executionEndTime = System.nanoTime();	
			long execNanos = executionEndTime - executionStartTime;
			query.setExecutionTime(Float.valueOf(execNanos)/ (1000*1000));
			return result;
		case EVALUATE_MEMORY_OCCUPATION :
			return dispatchQuery(query);
		default :
			throw new IllegalArgumentException("Unknown measurement type.");
		}
	}
}
