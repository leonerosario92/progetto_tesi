package impl.datasource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import datasource.DataSourceException;
import datasource.IRecordScanner;
import datasource.IRemoteDataSource;
import model.FieldDescriptor;
import model.TableDescriptor;

public abstract class JDBCDataSource implements IRemoteDataSource{
	
	private static final Integer DEFAULT_FETCH_SIZE = Integer.MIN_VALUE;
	
	private Connection connection;
	private JDBCMetaData metaData;
	private JDBCConnectionParams params;
	
	private PreparedStatement getTableStatement;
	
	public JDBCDataSource(JDBCConnectionParams params) throws JDBCDataSourceException {
		
		this.params = params;
		
		//TODO Manage exception properly
		try {
			this.connection = connect();
			if(connection instanceof com.mysql.jdbc.Connection) {
				((com.mysql.jdbc.Connection) connection).setUseCursorFetch(true);
				((com.mysql.jdbc.Connection) connection).setUseServerPrepStmts(false);
				((com.mysql.jdbc.Connection) connection).setDefaultFetchSize(DEFAULT_FETCH_SIZE);

			}
			this.getTableStatement = prepareGetTableStatement();
			this.metaData = new JDBCMetaData(connection);
		} catch (ClassNotFoundException e) {
			throw new JDBCDataSourceException("An error occurred while loading JDBC driver");
		} catch (SQLException e) {
			throw new JDBCDataSourceException ("An error occurred while connecting to remote DB"
					+ "caused by : " + e.getMessage());
		}
	}
	
	/* ---METHODS THAT SPECIFIC JDBC IMPLEMENTATION MUST EXTEND---*/
	protected abstract String getBaseUrl();
	
	protected abstract String getDriverClassName ();
	/*------------------------------------------------------------*/
	
	private Connection connect() throws SQLException, ClassNotFoundException {
		//Class.forName(getDriverClassName());
		connection = DriverManager
		        .getConnection(getConnectionString(params));
		return connection; 
	}
	
	
	private String getConnectionString(JDBCConnectionParams params) {
		StringBuilder connectionString = new StringBuilder();
    	connectionString.append(getBaseUrl());
    	connectionString.append(":");
    	connectionString.append("//");
    	connectionString.append(params.getHost());
    	connectionString.append(":");
    	connectionString.append(params.getPortNumber());
    	connectionString.append("/");
    	connectionString.append(params.getDbName());
    	connectionString.append("?");
    	connectionString.append("user=").append(params.getUsername());
    	connectionString.append("&");
    	connectionString.append("password=").append(params.getPassword());
    	return connectionString.toString();
	}
	
	
	private PreparedStatement getPreparedStatement(String query) throws SQLException {
		PreparedStatement statement =
		        connection.prepareStatement(query);
		return statement;
	}
	
	
	private PreparedStatement prepareGetTableStatement() throws SQLException {
		String query = "select * from tableName=?";
		return getPreparedStatement(query);
	}
	
	
	private String getColumnStatement(int columnCount) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		for(int i=0; i < columnCount; i++) {
			sb.append("{").append(i).append("}").append(" ");
			if(i != columnCount-1) {
				sb.append(", ");
			}else {
				sb.append(" ");
			}
		}
		sb.append("from ").append("{").append(columnCount).append("}").append(" ");
		return sb.toString();
	}
	
	
	/*---METHODS INHERITED FROM IRemoteDataSource ---*/
	public JDBCMetaData getMetaData() {
		return metaData;
	}
	
	
	public Connection getConnection () {
		return connection;
	}
	
	
	public void close() throws JDBCDataSourceException {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new JDBCDataSourceException("Error while attempting to close connection");
			}
		}
	}
	
	
	public IRecordScanner getTable(TableDescriptor table) throws JDBCDataSourceException  {
		PreparedStatement statement = getTableStatement;
		try {
			statement.setString(1,table.getName());
			ResultSet result = statement.executeQuery();
			int recordCount = table.getRecordCount();
			return new JDBCRecordScanner (result,recordCount);
		} catch (SQLException e) {
			throw new JDBCDataSourceException("An error occour while trying to retrieve table "+table.getName()+" from data source");
		}
	}


	@Override
	public IRecordScanner getTableProjection(TableDescriptor table, FieldDescriptor... args) throws DataSourceException {
		int fieldNum = args.length;
		try {
			String query = getColumnStatement(fieldNum);
			String[] values = new String[fieldNum+1];
			for(int i=0; i<fieldNum; i++) {
				FieldDescriptor field = args[i];
				String fieldName = field.getTable().getName()+"."+field.getName();
				values[i] = fieldName;
			}
			values[fieldNum] = table.getName();
			
			String formattedQuery = MessageFormat.format(query, values);
			Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery(formattedQuery);
			int recordCount = table.getRecordCount();
			
			return new JDBCRecordScanner(result,recordCount);
		} catch (SQLException e) {
			throw new DataSourceException("An error occour while trying to retrieve a set of column from remote data source");
		}
	}

	

	
}
